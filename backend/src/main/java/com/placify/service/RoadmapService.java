package com.placify.service;

import com.placify.dto.AnalysisResponse;
import com.placify.model.Skill;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    public List<AnalysisResponse.RoadmapItem> generateRoadmap(List<String> missingSkills, Set<Skill> requiredSkills) {
        Map<String, Skill> skillMap = requiredSkills.stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s, (a, b) -> a));

        List<AnalysisResponse.RoadmapItem> roadmap = new ArrayList<>();

        for (String skillName : missingSkills) {
            Skill skill = skillMap.get(skillName.toLowerCase());
            String description = (skill != null && skill.getLearningDescription() != null)
                    ? skill.getLearningDescription()
                    : "Learn " + skillName + " fundamentals and build projects";
            String resource = (skill != null) ? skill.getLearningResource() : null;

            roadmap.add(AnalysisResponse.RoadmapItem.builder()
                    .skill(skillName).description(description).resource(resource).build());
        }
        return roadmap;
    }
}
