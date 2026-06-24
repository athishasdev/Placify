package com.placify.service;

import com.placify.dto.AnalysisResponse;
import com.placify.model.*;
import com.placify.exception.ResourceNotFoundException;
import com.placify.repository.AnalysisReportRepository;
import com.placify.repository.JobRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final AnalysisReportRepository reportRepository;
    private final JobRoleRepository jobRoleRepository;
    private final RoadmapService roadmapService;

    public AnalysisService(AnalysisReportRepository reportRepository,
                           JobRoleRepository jobRoleRepository,
                           RoadmapService roadmapService) {
        this.reportRepository = reportRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.roadmapService = roadmapService;
    }

    @Transactional
    public AnalysisResponse analyze(User user, Resume resume, Long jobRoleId) {
        JobRole jobRole = jobRoleRepository.findById(jobRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found"));

        Set<String> studentSkills = resume.getDetectedSkills().stream()
                .map(s -> s.getName().toLowerCase())
                .collect(Collectors.toSet());

        Set<Skill> requiredSkills = jobRole.getRequiredSkills();
        int totalRequired = requiredSkills.size();

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (Skill reqSkill : requiredSkills) {
            if (studentSkills.contains(reqSkill.getName().toLowerCase())) {
                matchedSkills.add(reqSkill.getName());
            } else {
                missingSkills.add(reqSkill.getName());
            }
        }

        // Calculate match score
        double matchScore = totalRequired > 0
                ? Math.round((double) matchedSkills.size() / totalRequired * 100.0 * 10.0) / 10.0
                : 0.0;

        // Generate roadmap for missing skills
        List<AnalysisResponse.RoadmapItem> roadmap = roadmapService.generateRoadmap(missingSkills, requiredSkills);

        // Save report
        AnalysisReport report = AnalysisReport.builder()
                .user(user)
                .resume(resume)
                .jobRole(jobRole)
                .matchScore(matchScore)
                .matchedSkills(String.join(",", matchedSkills))
                .missingSkills(String.join(",", missingSkills))
                .roadmap(roadmap.stream()
                        .map(r -> r.getSkill() + ": " + r.getDescription())
                        .collect(Collectors.joining(";")))
                .build();

        reportRepository.save(report);

        Collections.sort(matchedSkills);
        Collections.sort(missingSkills);

        return AnalysisResponse.builder()
                .reportId(report.getId())
                .jobRoleName(jobRole.getRoleName())
                .matchScore(matchScore)
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .roadmap(roadmap)
                .totalRequired(totalRequired)
                .resumeFileName(resume.getFileName())
                .build();
    }

    public AnalysisResponse getReport(Long reportId) {
        AnalysisReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        List<String> matchedSkills = report.getMatchedSkills() != null && !report.getMatchedSkills().isEmpty()
                ? Arrays.asList(report.getMatchedSkills().split(","))
                : new ArrayList<>();

        List<String> missingSkills = report.getMissingSkills() != null && !report.getMissingSkills().isEmpty()
                ? Arrays.asList(report.getMissingSkills().split(","))
                : new ArrayList<>();

        // Rebuild roadmap from missing skills
        Set<Skill> requiredSkills = report.getJobRole().getRequiredSkills();
        List<AnalysisResponse.RoadmapItem> roadmap = roadmapService.generateRoadmap(missingSkills, requiredSkills);

        return AnalysisResponse.builder()
                .reportId(report.getId())
                .jobRoleName(report.getJobRole().getRoleName())
                .matchScore(report.getMatchScore())
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .roadmap(roadmap)
                .totalRequired(requiredSkills.size())
                .resumeFileName(report.getResume().getFileName())
                .build();
    }

    public List<AnalysisResponse> getUserReports(Long userId) {
        return reportRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(report -> {
                    List<String> matched = report.getMatchedSkills() != null && !report.getMatchedSkills().isEmpty()
                            ? Arrays.asList(report.getMatchedSkills().split(","))
                            : new ArrayList<>();
                    List<String> missing = report.getMissingSkills() != null && !report.getMissingSkills().isEmpty()
                            ? Arrays.asList(report.getMissingSkills().split(","))
                            : new ArrayList<>();

                    return AnalysisResponse.builder()
                            .reportId(report.getId())
                            .jobRoleName(report.getJobRole().getRoleName())
                            .matchScore(report.getMatchScore())
                            .matchedSkills(matched)
                            .missingSkills(missing)
                            .totalRequired(report.getJobRole().getRequiredSkills().size())
                            .resumeFileName(report.getResume().getFileName())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
