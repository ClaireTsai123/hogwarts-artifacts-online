package com.example.hogwartsartifactsonline.artifact.converter;

import com.example.hogwartsartifactsonline.artifact.Artifact;
import com.example.hogwartsartifactsonline.artifact.DTO.ArtifactDto;
import com.example.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private final WizardToWizardDtoConverter wizardConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardConverter) {
        this.wizardConverter = wizardConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImgUrl(),
                source.getOwner() != null
                        ? wizardConverter.convert(source.getOwner()) : null
        );
    }
}
