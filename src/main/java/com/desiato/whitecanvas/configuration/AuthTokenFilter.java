package com.desiato.whitecanvas.configuration;

import com.desiato.whitecanvas.dto.WhiteCanvasToken;
import com.desiato.whitecanvas.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public AuthTokenFilter(@Lazy AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenValue = request.getHeader("authToken");

        if (isPublicEndpoint(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenValue == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        try {
            WhiteCanvasToken whiteCanvasToken = new WhiteCanvasToken(tokenValue);
            var authenticationDetails = authenticationService.createUserDetails(whiteCanvasToken)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            if (authenticationDetails.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationDetails.get());
                filterChain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return "/api/login".equals(requestURI) || "/api/user".equals(requestURI);
    }
}
