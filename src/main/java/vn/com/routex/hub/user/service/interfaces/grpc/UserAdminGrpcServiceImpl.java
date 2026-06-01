package vn.com.routex.hub.user.service.interfaces.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.com.routex.hub.grpc.CustomerAdminInfo;
import vn.com.routex.hub.grpc.FetchCustomerByUserIdRequest;
import vn.com.routex.hub.grpc.FetchCustomerByUserIdResponse;
import vn.com.routex.hub.grpc.FetchCustomersByUserIdsRequest;
import vn.com.routex.hub.grpc.FetchCustomersByUserIdsResponse;
import vn.com.routex.hub.grpc.FetchUserAccountByEmailRequest;
import vn.com.routex.hub.grpc.FetchUserAccountByIdRequest;
import vn.com.routex.hub.grpc.FetchUserAccountResponse;
import vn.com.routex.hub.grpc.UserAdminGrpcServiceGrpc;
import vn.com.routex.hub.grpc.UserAccountInfo;
import vn.com.routex.hub.grpc.UserAdminRequestContext;
import vn.com.routex.hub.user.service.application.command.common.RequestContext;
import vn.com.routex.hub.user.service.application.service.internal.InternalCustomerAdminService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.domain.user.model.User;
import vn.com.routex.hub.user.service.domain.user.port.UserRepositoryPort;
import vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.utils.ExceptionUtils;

@GrpcService
@RequiredArgsConstructor
public class UserAdminGrpcServiceImpl extends UserAdminGrpcServiceGrpc.UserAdminGrpcServiceImplBase {

    private final InternalCustomerAdminService internalCustomerAdminService;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void fetchCustomerByUserId(FetchCustomerByUserIdRequest request,
                                      StreamObserver<FetchCustomerByUserIdResponse> responseObserver) {
        try {
            RequestContext context = toRequestContext(request.getContext());
            Customer customer = internalCustomerAdminService.fetchCustomerByUserId(request.getUserId(), context);
            FetchCustomerByUserIdResponse response = FetchCustomerByUserIdResponse.newBuilder()
                    .setCustomer(mapCustomerInfo(customer))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            handleException(ex, responseObserver);
        }
    }

    @Override
    public void fetchCustomersByUserIds(FetchCustomersByUserIdsRequest request,
                                        StreamObserver<FetchCustomersByUserIdsResponse> responseObserver) {
        try {
            RequestContext context = toRequestContext(request.getContext());
            var customers = internalCustomerAdminService.fetchCustomersByUserIds(request.getUserIdsList(), context);
            FetchCustomersByUserIdsResponse response = FetchCustomersByUserIdsResponse.newBuilder()
                    .addAllCustomers(customers.stream().map(this::mapCustomerInfo).toList())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            handleException(ex, responseObserver);
        }
    }

    @Override
    public void fetchUserAccountById(FetchUserAccountByIdRequest request,
                                     StreamObserver<FetchUserAccountResponse> responseObserver) {
        try {
            User user = userRepositoryPort.findById(request.getUserId())
                    .orElseThrow(() -> new BusinessException(
                            request.getContext().getRequestId(),
                            request.getContext().getRequestDateTime(),
                            request.getContext().getChannel(),
                            ExceptionUtils.buildResultResponse(ErrorConstant.RECORD_NOT_FOUND, "User account not found")
                    ));
            responseObserver.onNext(FetchUserAccountResponse.newBuilder()
                    .setUser(mapUserAccountInfo(user))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            handleException(ex, responseObserver);
        }
    }

    @Override
    public void fetchUserAccountByEmail(FetchUserAccountByEmailRequest request,
                                        StreamObserver<FetchUserAccountResponse> responseObserver) {
        try {
            User user = userRepositoryPort.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException(
                            request.getContext().getRequestId(),
                            request.getContext().getRequestDateTime(),
                            request.getContext().getChannel(),
                            ExceptionUtils.buildResultResponse(ErrorConstant.RECORD_NOT_FOUND, "User account not found")
                    ));
            responseObserver.onNext(FetchUserAccountResponse.newBuilder()
                    .setUser(mapUserAccountInfo(user))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            handleException(ex, responseObserver);
        }
    }

    private RequestContext toRequestContext(UserAdminRequestContext context) {
        if (context == null) {
            return RequestContext.builder().build();
        }
        return RequestContext.builder()
                .requestId(context.getRequestId())
                .requestDateTime(context.getRequestDateTime())
                .channel(context.getChannel())
                .build();
    }

    private CustomerAdminInfo mapCustomerInfo(Customer customer) {
        if (customer == null) {
            return CustomerAdminInfo.getDefaultInstance();
        }
        return CustomerAdminInfo.newBuilder()
                .setId(customer.getId() != null ? customer.getId() : "")
                .setUserId(customer.getUserId() != null ? customer.getUserId() : "")
                .setFullName(customer.getFullName() != null ? customer.getFullName() : "")
                .setStatus(customer.getStatus() != null ? customer.getStatus().name() : "")
                .setTotalTrips(customer.getTotalTrips() != null ? customer.getTotalTrips() : 0)
                .setTripPoints(customer.getTripPoints() != null ? customer.getTripPoints().toString() : "")
                .setTotalSpent(customer.getTotalSpent() != null ? customer.getTotalSpent().toString() : "")
                .setLastBookingAt(customer.getLastBookingAt() != null ? customer.getLastBookingAt().toString() : "")
                .setLastTripAt(customer.getLastTripAt() != null ? customer.getLastTripAt().toString() : "")
                .build();
    }

    private UserAccountInfo mapUserAccountInfo(User user) {
        if (user == null) {
            return UserAccountInfo.getDefaultInstance();
        }
        return UserAccountInfo.newBuilder()
                .setId(user.getId() != null ? user.getId() : "")
                .setEmail(user.getEmail() != null ? user.getEmail() : "")
                .setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : "")
                .setStatus(user.getStatus() != null ? user.getStatus().name() : "")
                .build();
    }

    private void handleException(Throwable ex, StreamObserver<?> responseObserver) {
        if (ex instanceof BusinessException businessEx) {
            String code = businessEx.getResult() != null ? businessEx.getResult().getResponseCode() : "99";
            String desc = businessEx.getResult() != null ? businessEx.getResult().getDescription() : ex.getMessage();
            Status status = Status.INTERNAL;
            if (ErrorConstant.RECORD_NOT_FOUND.equals(code)) {
                status = Status.NOT_FOUND;
            }
            responseObserver.onError(status.withDescription(desc).asRuntimeException());
        } else {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
