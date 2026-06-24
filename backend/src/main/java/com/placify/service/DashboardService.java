package com.placify.service;

import com.placify.dto.DashboardResponse;
import com.placify.model.*;
import com.placify.model.enums.UserRole;
import com.placify.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final JobRoleRepository jobRoleRepository;
    private final AnalysisReportRepository reportRepository;

    public DashboardService(UserRepository userRepository, ResumeRepository resumeRepository,
                            JobRoleRepository jobRoleRepository, AnalysisReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.reportRepository = reportRepository;
    }

    public DashboardResponse getStudentDashboard(User user) {
        Resume latestResume = resumeRepository.findTopByUserIdOrderByUploadedAtDesc(user.getId()).orElse(null);

        List<String> detectedSkills = latestResume != null
                ? latestResume.getDetectedSkills().stream().map(Skill::getName).sorted().collect(Collectors.toList())
                : new ArrayList<>();

        List<AnalysisReport> reports = reportRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<DashboardResponse.ReportSummary> summaries = reports.stream()
                .map(r -> DashboardResponse.ReportSummary.builder()
                        .reportId(r.getId())
                        .jobRoleName(r.getJobRole().getRoleName())
                        .matchScore(r.getMatchScore())
                        .createdAt(r.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .studentName(user.getName())
                .resumeFileName(latestResume != null ? latestResume.getFileName() : null)
                .detectedSkills(detectedSkills)
                .recentReports(summaries)
                .build();
    }

    public DashboardResponse getAdminDashboard() {
        long totalStudents = userRepository.countByRole(UserRole.STUDENT);
        long totalJobRoles = jobRoleRepository.count();
        long totalResumes = resumeRepository.count();
        long totalAnalyses = reportRepository.count();

        // Calculate most commonly missing skills
        List<String> allMissing = reportRepository.findAllMissingSkills();
        Map<String, Long> freq = new HashMap<>();
        for (String csv : allMissing) {
            if (csv != null && !csv.isEmpty()) {
                for (String skill : csv.split(",")) {
                    String trimmed = skill.trim();
                    if (!trimmed.isEmpty()) {
                        freq.merge(trimmed, 1L, Long::sum);
                    }
                }
            }
        }

        List<DashboardResponse.SkillFrequency> topMissing = freq.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> DashboardResponse.SkillFrequency.builder()
                        .skillName(e.getKey()).count(e.getValue()).build())
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalStudents(totalStudents)
                .totalJobRoles(totalJobRoles)
                .totalResumes(totalResumes)
                .totalAnalyses(totalAnalyses)
                .topMissingSkills(topMissing)
                .build();
    }
}
