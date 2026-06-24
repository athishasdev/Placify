package com.placify.service;

import com.placify.model.Skill;
import com.placify.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SkillExtractionService {

    private final SkillRepository skillRepository;

    // Map of skill aliases to canonical names for better matching
    private static final Map<String, List<String>> SKILL_ALIASES = new HashMap<>();

    static {
        SKILL_ALIASES.put("Java", Arrays.asList("java", "j2ee", "j2se", "jdk", "jre", "core java", "advanced java"));
        SKILL_ALIASES.put("Spring Boot", Arrays.asList("spring boot", "springboot", "spring-boot"));
        SKILL_ALIASES.put("Spring MVC", Arrays.asList("spring mvc", "springmvc", "spring-mvc"));
        SKILL_ALIASES.put("Hibernate", Arrays.asList("hibernate", "hibernate orm", "jpa/hibernate"));
        SKILL_ALIASES.put("SQL", Arrays.asList("sql", "structured query language", "sql queries"));
        SKILL_ALIASES.put("PostgreSQL", Arrays.asList("postgresql", "postgres", "psql", "pg"));
        SKILL_ALIASES.put("MySQL", Arrays.asList("mysql", "my sql"));
        SKILL_ALIASES.put("MongoDB", Arrays.asList("mongodb", "mongo db", "mongo"));
        SKILL_ALIASES.put("JavaScript", Arrays.asList("javascript", "js", "es6", "es2015", "ecmascript"));
        SKILL_ALIASES.put("TypeScript", Arrays.asList("typescript", "ts"));
        SKILL_ALIASES.put("Python", Arrays.asList("python", "python3", "py"));
        SKILL_ALIASES.put("C++", Arrays.asList("c++", "cpp", "c plus plus"));
        SKILL_ALIASES.put("HTML", Arrays.asList("html", "html5", "html/css"));
        SKILL_ALIASES.put("CSS", Arrays.asList("css", "css3", "html/css", "scss", "sass"));
        SKILL_ALIASES.put("React", Arrays.asList("react", "reactjs", "react.js", "react js"));
        SKILL_ALIASES.put("Angular", Arrays.asList("angular", "angularjs", "angular.js"));
        SKILL_ALIASES.put("Node.js", Arrays.asList("node.js", "nodejs", "node js", "express.js", "expressjs"));
        SKILL_ALIASES.put("REST API", Arrays.asList("rest api", "restful", "rest apis", "restful api", "rest", "restful apis", "web api", "web services"));
        SKILL_ALIASES.put("Git", Arrays.asList("git", "github", "gitlab", "bitbucket", "version control"));
        SKILL_ALIASES.put("Maven", Arrays.asList("maven", "apache maven", "mvn"));
        SKILL_ALIASES.put("Docker", Arrays.asList("docker", "dockerfile", "docker-compose", "containerization"));
        SKILL_ALIASES.put("OOP", Arrays.asList("oop", "oops", "object oriented", "object-oriented", "object oriented programming"));
        SKILL_ALIASES.put("DSA", Arrays.asList("dsa", "data structures", "algorithms", "data structures and algorithms"));
        SKILL_ALIASES.put("Design Patterns", Arrays.asList("design patterns", "design pattern", "gof patterns"));
        SKILL_ALIASES.put("System Design", Arrays.asList("system design", "system architecture", "software architecture"));
        SKILL_ALIASES.put("JUnit", Arrays.asList("junit", "junit5", "junit4", "unit testing", "test driven"));
        SKILL_ALIASES.put("Selenium", Arrays.asList("selenium", "selenium webdriver", "selenium ide"));
        SKILL_ALIASES.put("AWS", Arrays.asList("aws", "amazon web services", "ec2", "s3", "lambda"));
        SKILL_ALIASES.put("Spring Security", Arrays.asList("spring security", "springsecurity"));
    }

    public SkillExtractionService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Set<Skill> extractSkills(String text) {
        if (text == null || text.isBlank()) {
            return new HashSet<>();
        }

        String normalizedText = text.toLowerCase();
        Set<Skill> detectedSkills = new HashSet<>();

        // Check each skill from the database against the text
        List<Skill> allSkills = skillRepository.findAll();

        for (Skill skill : allSkills) {
            String skillName = skill.getName();

            // Check the canonical name
            if (containsSkill(normalizedText, skillName.toLowerCase())) {
                detectedSkills.add(skill);
                continue;
            }

            // Check aliases
            List<String> aliases = SKILL_ALIASES.get(skillName);
            if (aliases != null) {
                for (String alias : aliases) {
                    if (containsSkill(normalizedText, alias)) {
                        detectedSkills.add(skill);
                        break;
                    }
                }
            }
        }

        return detectedSkills;
    }

    private boolean containsSkill(String text, String skill) {
        // Use word boundary matching for short skills to avoid false positives
        // e.g., "CSS" shouldn't match "accessing"
        if (skill.length() <= 3) {
            String regex = "(?i)\\b" + Pattern.quote(skill) + "\\b";
            return Pattern.compile(regex).matcher(text).find();
        }
        return text.contains(skill);
    }
}
