package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public String authenticateAndGenerateToken(@RequestBody AuthenticationRequestDto request) {
        return authenticationService.authenticateAndGenerateToken(request);
    }
}
