package vn.com.routex.hub.user.service.interfaces.model.internal.customer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InternalFetchCustomersByUserIdsRequest {
    private List<String> userIds;
}
