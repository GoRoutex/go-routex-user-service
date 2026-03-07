package vn.com.routex.hub.user.service.interfaces.models.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;

@Getter
@Setter
@SuperBuilder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String requestId;
    private String requestDateTime;
    private String channel;
    private ApiResult result;
    private T data;
}
