package com.example.hogwartsartifactsonline.wizard.DTO;

import com.example.hogwartsartifactsonline.artifact.Artifact;

import java.util.List;

public record WizardDto(
    Integer id,
    String name,
    Integer numberOfArtifacts
) {

}
