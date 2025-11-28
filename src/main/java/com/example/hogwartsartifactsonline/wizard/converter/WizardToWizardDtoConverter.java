package com.example.hogwartsartifactsonline.wizard.converter;

import com.example.hogwartsartifactsonline.wizard.DTO.WizardDto;
import com.example.hogwartsartifactsonline.wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto> {

    @Override
    public WizardDto convert(Wizard source) {
        return new WizardDto(
                source.getId(),
                source.getName(),
                source.getNumberOfArtifacts()
        );
    }
}
