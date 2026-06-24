package com.placify.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardResponse {

    // Student dashboard fields
    private String studentName;
    private String resumeFileName;
    private List<String> detectedSkills;
    private List<ReportSummary> recentReports;

    // Admin dashboard fields
    private Long totalStudents;
    private Long totalJobRoles;
    private Long totalResumes;
    private Long totalAnalyses;
    private List<SkillFrequency> topMissingSkills;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class ReportSummary {
        private Long reportId;
        private String jobRoleName;
        private Double matchScore;
        private String createdAt;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class SkillFrequency {
        private String skillName;
        private Long count;
    }
}
