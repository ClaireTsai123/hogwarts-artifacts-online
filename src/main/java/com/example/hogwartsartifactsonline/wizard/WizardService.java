package com.example.hogwartsartifactsonline.wizard;

import com.example.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WizardService {
    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;

    public Wizard findWizardById(Integer id) {
        return wizardRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("wizard",id));
    }

    public List<Wizard> findAllWizards() {
        return wizardRepository.findAll();
    }

    public Wizard saveWizard(Wizard wizard) {
        return wizardRepository.save(wizard);
    }

    public Wizard updateWizard(Integer id, Wizard updatedWizard) {
        return wizardRepository.findById(id).map(existingWizard -> {
            existingWizard.setName(updatedWizard.getName());
            return wizardRepository.save(existingWizard);
        }).orElseThrow(() -> new ObjectNotFoundException("wizard",id));
    }

    public void deleteWizard(Integer wizardId) {
       Wizard wizardToBeDeleted =  wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard",wizardId));
        //before deletion, we will unassign this wizard's owned artifacts
        wizardToBeDeleted.removeAllArtifacts();
        wizardRepository.deleteById(wizardId);
    }

    public void addArtifactToWizard(Integer wizardId, String artifactId) {
        artifactRepository.findById(artifactId).map(
                        artifact -> {
                            Wizard wizard = findWizardById(wizardId);
                            wizard.addArtifact(artifact);
                            return wizardRepository.save(wizard);
                        })
                .orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));
    }
}
