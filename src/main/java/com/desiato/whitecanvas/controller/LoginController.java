package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.dto.LoginResponseDTO;
import com.desiato.whitecanvas.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final TokenService tokenService;

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody AuthenticationRequestDto requestDTO) {
        String token = tokenService.authenticateAndGenerateToken(requestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(token, "success"));
    }
}
