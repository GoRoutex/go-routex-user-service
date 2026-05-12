package vn.com.routex.hub.user.service.interfaces.controller.internal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.service.internal.InternalCustomerAdminService;
import vn.com.routex.hub.user.service.domain.customer.model.Customer;
import vn.com.routex.hub.user.service.infrastructure.utils.ApiRequestUtils;
import vn.com.routex.hub.user.service.interfaces.model.internal.customer.InternalCustomerResponses;
import vn.com.routex.hub.user.service.interfaces.model.internal.customer.InternalFetchCustomersByUserIdsRequest;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseRequest;
import vn.com.routex.hub.user.service.interfaces.models.base.BaseResponse;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

import java.util.List;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.CUSTOMERS_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.DETAIL_BY_USER_ID;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.FETCH_BY_USER_IDS;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.INTERNAL_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE + INTERNAL_PATH + CUSTOMERS_PATH)
public class InternalCustomerAdminController {

    private final InternalCustomerAdminService internalCustomerAdminService;

    @GetMapping(DETAIL_BY_USER_ID)
    public ResponseEntity<BaseResponse<InternalCustomerResponses.CustomerData>> fetchCustomerByUserId(
            HttpServletRequest servletRequest,
            @RequestParam String userId
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        Customer customer = internalCustomerAdminService.fetchCustomerByUserId(userId, toContext(baseRequest));

        BaseResponse<InternalCustomerResponses.CustomerData> response =
                BaseResponse.<InternalCustomerResponses.CustomerData>builder()
                        .requestId(baseRequest.getRequestId())
                        .requestDateTime(baseRequest.getRequestDateTime())
                        .channel(baseRequest.getChannel())
                        .result(ApiResult.builder().responseCode(SUCCESS_CODE).description(SUCCESS_MESSAGE).build())
                        .data(toCustomerData(customer))
                        .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(FETCH_BY_USER_IDS)
    public ResponseEntity<BaseResponse<InternalCustomerResponses.CustomerListData>> fetchCustomersByUserIds(
            HttpServletRequest servletRequest,
            @RequestBody InternalFetchCustomersByUserIdsRequest request
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        List<Customer> customers = internalCustomerAdminService.fetchCustomersByUserIds(request.getUserIds(), toContext(baseRequest));

        BaseResponse<InternalCustomerResponses.CustomerListData> response =
                BaseResponse.<InternalCustomerResponses.CustomerListData>builder()
                        .requestId(baseRequest.getRequestId())
                        .requestDateTime(baseRequest.getRequestDateTime())
                        .channel(baseRequest.getChannel())
                        .result(ApiResult.builder().responseCode(SUCCESS_CODE).description(SUCCESS_MESSAGE).build())
                        .data(InternalCustomerResponses.CustomerListData.builder()
                                .items(customers.stream().map(this::toCustomerData).toList())
                                .build())
                        .build();
        return ResponseEntity.ok(response);
    }

    private InternalCustomerResponses.CustomerData toCustomerData(Customer customer) {
        return InternalCustomerResponses.CustomerData.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .fullName(customer.getFullName())
                .status(customer.getStatus())
                .totalTrips(customer.getTotalTrips())
                .tripPoints(customer.getTripPoints())
                .totalSpent(customer.getTotalSpent())
                .lastBookingAt(customer.getLastBookingAt())
                .lastTripAt(customer.getLastTripAt())
                .build();
    }

    private RequestContext toContext(BaseRequest request) {
        return RequestContext.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .build();
    }
}
