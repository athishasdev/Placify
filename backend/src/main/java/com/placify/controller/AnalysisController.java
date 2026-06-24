package com.placify.controller;

import com.placify.dto.AnalysisResponse;
import com.placify.dto.ApiResponse;
import com.placify.model.Resume;
import com.placify.model.User;
import com.placify.service.AnalysisService;
import com.placify.service.AuthService;
import com.placify.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AuthService authService;
    private final ResumeService resumeService;

    public AnalysisController(AnalysisService analysisService, AuthService authService, ResumeService resumeService) {
        this.analysisService = analysisService;
        this.authService = authService;
        this.resumeService = resumeService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<AnalysisResponse>> analyze(@RequestBody Map<String, Long> request) {
        User user = authService.getCurrentUser();
        Long jobRoleId = request.get("jobRoleId");
        Long resumeId = request.get("resumeId");

        if (jobRoleId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Job role ID is required"));
        }

        Resume resume;
        if (resumeId != null) {
            resume = resumeService.getResumeById(resumeId);
        } else {
            resume = resumeService.getLatestResume(user.getId());
        }

        if (resume == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No resume found. Please upload a resume first."));
        }

        AnalysisResponse result = analysisService.analyze(user, resume, jobRoleId);
        return ResponseEntity.ok(ApiResponse.success("Analysis complete", result));
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Report retrieved", analysisService.getReport(id)));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> getMyReports() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Reports retrieved", analysisService.getUserReports(user.getId())));
    }
}
