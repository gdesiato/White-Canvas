package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.service.TokenService;
import com.desiato.whitecanvas.validator.AuthenticationRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;
    private final AuthenticationRequestValidator validator;

    @PostMapping("/token")
    public String authenticateAndGenerateToken(@RequestBody AuthenticationRequestDto request) {

        validator.validateAuthenticationRequestDto(request);

        return tokenService.authenticateAndGenerateToken(request);
    }
}
