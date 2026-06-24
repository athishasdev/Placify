package com.placify.controller;

import com.placify.dto.ApiResponse;
import com.placify.dto.JobRoleDTO;
import com.placify.service.JobRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class JobRoleController {

    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobRoleDTO>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.success("Roles retrieved", jobRoleService.getAllRoles()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobRoleDTO>> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Role retrieved", jobRoleService.getRoleById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobRoleDTO>> createRole(@Valid @RequestBody JobRoleDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Role created", jobRoleService.createRole(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobRoleDTO>> updateRole(@PathVariable Long id, @Valid @RequestBody JobRoleDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Role updated", jobRoleService.updateRole(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        jobRoleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted"));
    }
}
