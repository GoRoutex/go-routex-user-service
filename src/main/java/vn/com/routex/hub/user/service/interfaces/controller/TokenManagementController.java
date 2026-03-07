package vn.com.routex.hub.user.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.user.service.application.facade.AuthenticationFacade;
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenRequest;
import vn.com.routex.hub.user.service.interfaces.models.token.RefreshTokenResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.REFRESH_TOKEN;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ApiConstant.USER_SERVICE;

@RestController
@RequestMapping(API_PATH + API_VERSION + USER_SERVICE)
@RequiredArgsConstructor
public class TokenManagementController {


    private final AuthenticationFacade authenticationFacade;

    @PostMapping(REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authenticationFacade.refreshToken(request);
    }
}
