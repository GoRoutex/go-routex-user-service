package vn.com.routex.hub.user.service.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenCommand;
import vn.com.routex.hub.user.service.application.dto.authentication.RefreshTokenResult;
import vn.com.routex.hub.user.service.application.dto.common.RequestContext;
import vn.com.routex.hub.user.service.application.service.AuthenticationService;
import vn.com.routex.hub.user.service.interfaces.models.result.ApiResult;
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenRequest;
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.REFRESH_TOKEN;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE)
@RequiredArgsConstructor
public class TokenManagementController {

    private final AuthenticationService authenticationService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResult result = authenticationService.refreshToken(RefreshTokenCommand.builder()
                .context(RequestContext.builder()
                        .requestId(request.getRequestId())
                        .requestDateTime(request.getRequestDateTime())
                        .channel(request.getChannel())
                        .build())
                .refreshToken(request.getData().getRefreshToken())
                .build());

        return ResponseEntity.ok(RefreshTokenResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(ApiResult.builder()
                        .responseCode(SUCCESS_CODE)
                        .description(SUCCESS_MESSAGE)
                        .build())
                .data(RefreshTokenResponse.RefreshTokenResponseData.builder()
                        .accessToken(result.getAccessToken())
                        .refreshToken(result.getRefreshToken())
                        .userId(result.getUserId())
                        .accessTokenExpiredAt(result.getAccessTokenExpiredAt())
                        .refreshTokenExpiredAt(result.getRefreshTokenExpiredAt())
                        .build())
                .build());
    }
}
