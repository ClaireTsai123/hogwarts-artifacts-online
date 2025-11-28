package com.example.hogwartsartifactsonline.artifact.DTO;

import com.example.hogwartsartifactsonline.wizard.DTO.WizardDto;
import com.example.hogwartsartifactsonline.wizard.Wizard;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtifactDto(
        String id,
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Description cannot be empty")
        String description,
        @NotEmpty(message = "Image URL cannot be empty")
        String imageUrl,
        WizardDto owner
) {
}
