package vn.com.routex.hub.user.service.infrastructure.persistence.adapter.ticket;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.grpc.GetTicketByCustomerRequest;
import vn.com.routex.hub.grpc.GetTicketByCustomerResponse;
import vn.com.routex.hub.grpc.GetTicketByIdRequest;
import vn.com.routex.hub.grpc.GetTicketByIdResponse;
import vn.com.routex.hub.grpc.MerchantGrpcServiceGrpc;
import vn.com.routex.hub.grpc.TicketData;
import vn.com.routex.hub.user.service.domain.ticket.TicketStatus;
import vn.com.routex.hub.user.service.domain.ticket.model.Ticket;
import vn.com.routex.hub.user.service.domain.ticket.port.TicketRepositoryPort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TicketRepositoryGrpcAdapter implements TicketRepositoryPort {

    @GrpcClient("merchantService")
    private MerchantGrpcServiceGrpc.MerchantGrpcServiceBlockingStub merchantServiceStub;

    @Override
    public Ticket save(Ticket ticket) {
        throw new UnsupportedOperationException("save not supported in user service gRPC client adapter");
    }

    @Override
    public Optional<Ticket> findById(String id) {
        try {
            GetTicketByIdRequest request = GetTicketByIdRequest.newBuilder()
                    .setTicketId(id)
                    .build();
            GetTicketByIdResponse response = merchantServiceStub.getTicketById(request);
            if (response.hasTicket() && !response.getTicket().getId().isEmpty()) {
                return Optional.ofNullable(mapToDomain(response.getTicket()));
            }
            return Optional.empty();
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.INVALID_ARGUMENT || e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public Optional<Ticket> findByTicketCode(String ticketCode) {
        throw new UnsupportedOperationException("findByTicketCode not supported in user service gRPC client adapter");
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("findAll not supported in user service gRPC client adapter");
    }

    @Override
    public Page<Ticket> search(String query, Pageable pageable) {
        throw new UnsupportedOperationException("search not supported in user service gRPC client adapter");
    }

    @Override
    public String generateTicketCode() {
        throw new UnsupportedOperationException("generateTicketCode not supported in user service gRPC client adapter");
    }

    @Override
    public long countByMerchantId(String merchantId) {
        throw new UnsupportedOperationException("countByMerchantId not supported in user service gRPC client adapter");
    }

    @Override
    public Page<Ticket> findAllByMerchantId(String merchantId, Pageable pageable) {
        throw new UnsupportedOperationException("findAllByMerchantId not supported in user service gRPC client adapter");
    }

    @Override
    public Page<Ticket> findByCustomer(String email, String phone, String ticketCode,
                                       OffsetDateTime fromDate, OffsetDateTime toDate,
                                       Pageable pageable) {
        try {
            GetTicketByCustomerRequest request = GetTicketByCustomerRequest.newBuilder()
                    .setEmail(email != null ? email : "")
                    .setPhone(phone != null ? phone : "")
                    .setTicketCode(ticketCode != null ? ticketCode : "")
                    .setPage(pageable.getPageNumber() + 1)
                    .setSize(pageable.getPageSize())
                    .build();

            GetTicketByCustomerResponse response = merchantServiceStub.getTicketByCustomer(request);
            
            List<Ticket> tickets = response.getTicketsList().stream()
                    .map(this::mapToDomain)
                    .toList();

            return new PageImpl<>(tickets, pageable, response.getTotalElements());
        } catch (Exception e) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
    }

    private Ticket mapToDomain(TicketData data) {
        if (data == null || data.getId().isEmpty()) {
            return null;
        }
        return Ticket.builder()
                .id(data.getId())
                .ticketCode(data.getTicketCode())
                .bookingId(data.getBookingId())
                .bookingSeatId(data.getBookingSeatId())
                .merchantId(data.getMerchantId())
                .tripId(data.getTripId())
                .vehicleId(data.getVehicleId())
                .seatNumber(data.getSeatNumber())
                .customerName(data.getCustomerName())
                .customerPhone(data.getCustomerPhone())
                .customerEmail(data.getCustomerEmail())
                .price(data.getPrice().isEmpty() ? BigDecimal.ZERO : new BigDecimal(data.getPrice()))
                .status(data.getStatus().isEmpty() ? null : TicketStatus.valueOf(data.getStatus()))
                .issuedAt(data.getIssuedAt().isEmpty() ? null : OffsetDateTime.parse(data.getIssuedAt()))
                .checkedInAt(data.getCheckedInAt().isEmpty() ? null : OffsetDateTime.parse(data.getCheckedInAt()))
                .boardedAt(data.getBoardedAt().isEmpty() ? null : OffsetDateTime.parse(data.getBoardedAt()))
                .cancelledAt(data.getCancelledAt().isEmpty() ? null : OffsetDateTime.parse(data.getCancelledAt()))
                .checkedInBy(data.getCheckedInBy())
                .boardedBy(data.getBoardedBy())
                .cancelledBy(data.getCancelledBy())
                .pickupType(data.getPickupType())
                .pickupStopId(data.getPickupStopId())
                .pickupAddress(data.getPickupAddress())
                .dropoffType(data.getDropoffType())
                .dropoffStopId(data.getDropoffStopId())
                .dropoffAddress(data.getDropoffAddress())
                .build();
    }
}
