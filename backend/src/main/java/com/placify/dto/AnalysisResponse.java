package com.placify.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AnalysisResponse {
    private Long reportId;
    private String jobRoleName;
    private Double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<RoadmapItem> roadmap;
    private Integer totalRequired;
    private String resumeFileName;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class RoadmapItem {
        private String skill;
        private String description;
        private String resource;
    }
}
