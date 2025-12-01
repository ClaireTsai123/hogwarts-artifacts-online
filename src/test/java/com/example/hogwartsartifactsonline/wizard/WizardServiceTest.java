package com.example.hogwartsartifactsonline.wizard;

import com.example.hogwartsartifactsonline.artifact.Artifact;
import com.example.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;
    @Mock
    ArtifactRepository artifactRepository;
    @InjectMocks
    WizardService wizardService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() {
        //GIVEN
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImgUrl("ImageUrl");
        wizard.addArtifact(a1);
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        //WHEN
        Wizard found = wizardService.findWizardById(1);
        //THEN
        assertThat(found.getId()).isEqualTo(wizard.getId());
        assertThat(found.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testFindWizardByIdNotFound() {
        //GIVEN
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
        //WHEN
        Throwable throwable = catchThrowable(() -> wizardService.findWizardById(99));
        //THEN
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find wizard with id: 99.");
        verify(wizardRepository, times(1)).findById(99);
    }

    @Test
    void testFindAllWizardsSuccess() {
        //GIVEN
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        given(wizardRepository.findAll()).willReturn(List.of(w1, w2));
        //WHEN
        List<Wizard> wizards = wizardService.findAllWizards();
        //THEN
        assertThat(wizards).hasSize(2);
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveWizardSuccess() {
        //GIVEN
        Wizard wizard = new Wizard();
        wizard.setId(4);
        wizard.setName("Hermione Granger");
        given(wizardRepository.save(wizard)).willReturn(wizard);
        //WHEN
        Wizard result = wizardService.saveWizard(wizard);
        //THEN
        assertThat(result.getId()).isEqualTo(wizard.getId());
        assertThat(result.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository, times(1)).save(wizard);
    }

    @Test
    void testUpdateWizardSuccess() {
        //GIVEN
        Wizard existingWizard = new Wizard();
        existingWizard.setId(1);
        existingWizard.setName("Albus Dumbledore");

        Wizard updatedWizard = new Wizard();
        updatedWizard.setName("New Name");

        given(wizardRepository.findById(1)).willReturn(Optional.of(existingWizard));
        given(wizardRepository.save(existingWizard)).willReturn(existingWizard);
        //WHEN
        Wizard result = wizardService.updateWizard(1, updatedWizard);
        //THEN
        assertThat(result.getId()).isEqualTo(existingWizard.getId());
        assertThat(result.getName()).isEqualTo(updatedWizard.getName());
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(existingWizard);
    }

    @Test
    void testDeleteSuccess() {
        //GIVEN
        Wizard existingWizard = new Wizard();
        existingWizard.setId(1);
        existingWizard.setName("Albus Dumbledore");
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImgUrl("ImageUrl");
        existingWizard.addArtifact(a1);

        given(wizardRepository.findById(1)).willReturn(Optional.of(existingWizard));
        doNothing().when(wizardRepository).deleteById(1);
        //WHEN
        wizardService.deleteWizard(1);
        //THEN
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).deleteById(1);

    }

    @Test
    void deleteWizardNotFound() {
        //GIVEN
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
        //WHEN
        assertThrows(ObjectNotFoundException.class,
                () -> wizardService.deleteWizard(99));
        //THEN
        verify(wizardRepository, times(1)).findById(99);
    }

    @Test
    void testAssignArtifactToWizardSuccess() {
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904196");
        artifact.setName("Resurrection Stone");
        artifact.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        artifact.setImgUrl("ImageUrl");
        wizard.addArtifact(artifact);

        given(artifactRepository.findById("1250808601744904196")).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        given(wizardRepository.save(wizard)).willReturn(wizard);
        //when
        wizardService.addArtifactToWizard(1, "1250808601744904196");
        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904196");

    }

    @Test
    void testAssignArtifactNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> wizardService.addArtifactToWizard(1, "9999999999999999999"));
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id: 9999999999999999999.");
        verify(artifactRepository, times(1)).findById("9999999999999999999");
    }
}