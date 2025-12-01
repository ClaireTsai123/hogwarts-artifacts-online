package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.artifact.DTO.ArtifactDto;
import com.example.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.example.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwartsartifactsonline.system.Result;
import com.example.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoConverter;


    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId) {
        Artifact artifact = artifactService.getById(artifactId);
        ArtifactDto artifactDto = artifactConverter.convert(artifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> allArtifacts = artifactService.getAllArtifacts();
        List<ArtifactDto> artifactDtos = allArtifacts.stream().map(artifactConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDtos);
    }

    @PostMapping
    public Result saveArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
        Artifact artifact = artifactDtoConverter.convert(artifactDto);
        Artifact savedArtifact = artifactService.save(artifact);
        ArtifactDto savedArtifactDto = artifactConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
        Artifact toArtifact = artifactDtoConverter.convert(artifactDto);
        Artifact updatedArtifact = artifactService.update(artifactId, toArtifact);
        ArtifactDto updatedartifactDto = artifactConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedartifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        artifactService.deleteById(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
