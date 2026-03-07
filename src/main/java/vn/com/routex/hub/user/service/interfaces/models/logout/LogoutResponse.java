package vn.com.routex.hub.user.service.interfaces.models.logout;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutResponse {
    private String requestId;
    private String requestDateTime;
    private String channel;
    private ApiResult result;
}
