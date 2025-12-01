package com.example.hogwartsartifactsonline.wizard.DTO;

import jakarta.validation.constraints.NotEmpty;


public record WizardDto(
    Integer id,
    @NotEmpty(message = "Name can not be empty")
    String name,
    Integer numberOfArtifacts
) {

}
