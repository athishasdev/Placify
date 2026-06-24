package com.placify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JobRoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;

    @NotEmpty(message = "Required skills list cannot be empty")
    private List<String> requiredSkills;

    private Integer minMatchThreshold;
}
