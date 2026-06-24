package com.placify.controller;

import com.placify.dto.ApiResponse;
import com.placify.dto.DashboardResponse;
import com.placify.model.User;
import com.placify.service.AuthService;
import com.placify.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthService authService;

    public DashboardController(DashboardService dashboardService, AuthService authService) {
        this.dashboardService = dashboardService;
        this.authService = authService;
    }

    @GetMapping("/student")
    public ResponseEntity<ApiResponse<DashboardResponse>> studentDashboard() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Dashboard loaded", dashboardService.getStudentDashboard(user)));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<DashboardResponse>> adminDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard loaded", dashboardService.getAdminDashboard()));
    }
}
