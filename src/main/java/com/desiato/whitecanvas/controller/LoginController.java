package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.dto.LoginResponseDTO;
import com.desiato.whitecanvas.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody AuthenticationRequestDto requestDTO) {
        String token = authenticationService.authenticateAndGenerateToken(requestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(token, "success"));
    }
}
