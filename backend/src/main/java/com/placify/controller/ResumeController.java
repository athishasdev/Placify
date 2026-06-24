package com.placify.controller;

import com.placify.dto.ApiResponse;
import com.placify.model.Resume;
import com.placify.model.Skill;
import com.placify.model.User;
import com.placify.service.AuthService;
import com.placify.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final AuthService authService;

    public ResumeController(ResumeService resumeService, AuthService authService) {
        this.resumeService = resumeService;
        this.authService = authService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            User user = authService.getCurrentUser();
            Resume resume = resumeService.uploadAndProcess(file, user);

            List<String> skills = resume.getDetectedSkills().stream()
                    .map(Skill::getName).sorted().collect(Collectors.toList());

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("resumeId", resume.getId());
            data.put("fileName", resume.getFileName());
            data.put("detectedSkills", skills);
            data.put("skillCount", skills.size());

            return ResponseEntity.ok(ApiResponse.success("Resume uploaded and processed", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        List<String> skills = resume.getDetectedSkills().stream()
                .map(Skill::getName).sorted().collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("resumeId", resume.getId());
        data.put("fileName", resume.getFileName());
        data.put("detectedSkills", skills);
        data.put("uploadedAt", resume.getUploadedAt().toString());

        return ResponseEntity.ok(ApiResponse.success("Resume retrieved", data));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLatestResume() {
        User user = authService.getCurrentUser();
        Resume resume = resumeService.getLatestResume(user.getId());
        if (resume == null) {
            return ResponseEntity.ok(ApiResponse.error("No resume uploaded yet"));
        }

        List<String> skills = resume.getDetectedSkills().stream()
                .map(Skill::getName).sorted().collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("resumeId", resume.getId());
        data.put("fileName", resume.getFileName());
        data.put("detectedSkills", skills);
        data.put("uploadedAt", resume.getUploadedAt().toString());

        return ResponseEntity.ok(ApiResponse.success("Latest resume", data));
    }
}
