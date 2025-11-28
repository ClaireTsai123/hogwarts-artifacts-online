package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.artifact.DTO.ArtifactDto;
import com.example.hogwartsartifactsonline.utils.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final IdGenerator idGenerator;

    public Artifact getById(String artifactId) {
        return artifactRepository.findById(artifactId).orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> getAllArtifacts() {
        return artifactRepository.findAll();
    }

    public Artifact save(Artifact artifact) {
        artifact.setId(idGenerator.nextId());
        return artifactRepository.save(artifact);
    }

    public Artifact update(String id, Artifact updated) {
        return artifactRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setImgUrl(updated.getImgUrl());
            return artifactRepository.save(existing);
        }).orElseThrow(() -> new ArtifactNotFoundException(id));
    }

    public void deleteById(String artifactId) {
      artifactRepository.findById(artifactId).orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        artifactRepository.deleteById(artifactId);
    }
}
