package com.placify.service;

import com.placify.dto.JobRoleDTO;
import com.placify.model.JobRole;
import com.placify.model.Skill;
import com.placify.exception.ResourceNotFoundException;
import com.placify.repository.JobRoleRepository;
import com.placify.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;
    private final SkillRepository skillRepository;

    public JobRoleService(JobRoleRepository jobRoleRepository, SkillRepository skillRepository) {
        this.jobRoleRepository = jobRoleRepository;
        this.skillRepository = skillRepository;
    }

    public List<JobRoleDTO> getAllRoles() {
        return jobRoleRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public JobRoleDTO getRoleById(Long id) {
        JobRole role = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found with id: " + id));
        return toDTO(role);
    }

    @Transactional
    public JobRoleDTO createRole(JobRoleDTO dto) {
        if (jobRoleRepository.existsByRoleNameIgnoreCase(dto.getRoleName())) {
            throw new IllegalArgumentException("Job role with this name already exists");
        }

        Set<Skill> skills = resolveSkills(dto.getRequiredSkills());

        JobRole role = JobRole.builder()
                .roleName(dto.getRoleName())
                .description(dto.getDescription())
                .requiredSkills(skills)
                .minMatchThreshold(dto.getMinMatchThreshold() != null ? dto.getMinMatchThreshold() : 50)
                .build();

        return toDTO(jobRoleRepository.save(role));
    }

    @Transactional
    public JobRoleDTO updateRole(Long id, JobRoleDTO dto) {
        JobRole role = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found with id: " + id));

        Set<Skill> skills = resolveSkills(dto.getRequiredSkills());

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setRequiredSkills(skills);
        role.setMinMatchThreshold(dto.getMinMatchThreshold() != null ? dto.getMinMatchThreshold() : 50);

        return toDTO(jobRoleRepository.save(role));
    }

    public void deleteRole(Long id) {
        if (!jobRoleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job role not found with id: " + id);
        }
        jobRoleRepository.deleteById(id);
    }

    private Set<Skill> resolveSkills(List<String> skillNames) {
        Set<Skill> skills = new HashSet<>();
        for (String name : skillNames) {
            Skill skill = skillRepository.findByNameIgnoreCase(name.trim())
                    .orElseGet(() -> {
                        Skill newSkill = Skill.builder()
                                .name(name.trim())
                                .category("Other")
                                .learningDescription("Learn " + name.trim() + " fundamentals and best practices")
                                .build();
                        return skillRepository.save(newSkill);
                    });
            skills.add(skill);
        }
        return skills;
    }

    private JobRoleDTO toDTO(JobRole role) {
        return JobRoleDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .requiredSkills(role.getRequiredSkills().stream()
                        .map(Skill::getName)
                        .sorted()
                        .collect(Collectors.toList()))
                .minMatchThreshold(role.getMinMatchThreshold())
                .build();
    }
}
