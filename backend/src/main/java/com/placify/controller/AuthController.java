package com.placify.controller;

import jakarta.servlet.http.HttpSession;
import com.placify.dto.ApiResponse;
import com.placify.dto.LoginRequest;
import com.placify.dto.RegisterRequest;
import com.placify.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
    import org.springframework.security.web.context.HttpSessionSecurityContextRepository;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse<Map<String, Object>> response = authService.register(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


@PostMapping("/login")
public ResponseEntity<ApiResponse<Map<String, Object>>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest requestServlet) {

    Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    ));

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    HttpSession session = requestServlet.getSession(true);

    session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            context
    );

    ApiResponse<Map<String, Object>> response =
            authService.buildLoginResponse(request.getEmail());

    return ResponseEntity.ok(response);
}

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser() {
        try {
            var user = authService.getCurrentUser();
            Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole().name()
            );
            return ResponseEntity.ok(ApiResponse.success("Authenticated", data));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
    }
}
