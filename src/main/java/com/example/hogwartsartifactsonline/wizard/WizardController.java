package com.example.hogwartsartifactsonline.wizard;

import com.example.hogwartsartifactsonline.system.Result;
import com.example.hogwartsartifactsonline.system.StatusCode;
import com.example.hogwartsartifactsonline.wizard.DTO.WizardDto;
import com.example.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.example.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    public final WizardService wizardService;
    public final WizardToWizardDtoConverter wizardConverter;
    public final WizardDtoToWizardConverter wizardDtoConverter;

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard wizard = wizardService.findWizardById(wizardId);
        WizardDto wizardDto = wizardConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @GetMapping
    public Result findAllWizards() {
        List<WizardDto> wizardDtos = wizardService.findAllWizards().stream().map(wizardConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }

    @PostMapping
    public Result saveWizard(@Valid @RequestBody WizardDto wizard) {
        Wizard toSave = wizardDtoConverter.convert(wizard);
        Wizard savedWizard = wizardService.saveWizard(toSave);
        WizardDto savedWizardDto = wizardConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updatedWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizard) {
        Wizard toUpdate = wizardDtoConverter.convert(wizard);
        Wizard updatedWizard = wizardService.updateWizard(wizardId, toUpdate);
        WizardDto updatedWizardDto = wizardConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        wizardService.deleteWizard(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result addArtifactToWizard(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        wizardService.addArtifactToWizard(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }

}
