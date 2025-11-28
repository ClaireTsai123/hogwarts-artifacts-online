package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.utils.IdGenerator;
import com.example.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdGenerator idGenerator;
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts = new ArrayList<>();
    @BeforeEach
    void setUp() {

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImgUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImgUrl("ImageUrl");

        artifacts.add(a1);
        artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetByIdSuccess() {
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImgUrl("ImageUrl");
        Wizard owner = new Wizard();
        owner.setId(2);
        owner.setName("Harry Potter");
        a.setOwner(owner);
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        //When
        Artifact result = artifactService.getById("1250808601744904192");
        //Then
        assertThat(result.getId()).isEqualTo(a.getId());
        assertThat(result.getName()).isEqualTo(a.getName());
        assertThat(result.getDescription()).isEqualTo(a.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(a.getImgUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(()-> artifactService.getById("1250808601744904192"));
        //Then
        assertThat(throwable).
                isInstanceOf(ArtifactNotFoundException.class)
                        .hasMessage("Could not found artifact with id: 1250808601744904192.");
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);
        //When and Then
        List<Artifact> result = artifactService.getAllArtifacts();
        assertThat(result.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveArtifactSuccess() {
        //Given
        Artifact a = new Artifact();
        a.setName("fake name");
        a.setDescription("fake description");
        a.setImgUrl("fake ImageUrl");
        given(idGenerator.nextId()).willReturn("123456");
        given(artifactRepository.save(a)).willReturn(a);
        //When
        Artifact result = artifactService.save(a);
        //Then
        assertThat(result.getId()).isEqualTo("123456");
        assertThat(result.getName()).isEqualTo(a.getName());
        assertThat(result.getDescription()).isEqualTo(a.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(a.getImgUrl());
        verify(artifactRepository, times(1)).save(a);
    }

    @Test
    void testUpdateArtifactSuccess() {
        //Given
        Artifact existing = new Artifact();
        existing.setId("1250808601744904192");
        existing.setName("Invisibility Cloak");
        existing.setDescription("An invisibility cloak is used to make the wearer invisible.");
        existing.setImgUrl("ImageUrl");

        Artifact updated = new Artifact();
        updated.setName("Updated Name");
        updated.setDescription("Updated Description");
        updated.setImgUrl("Updated ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(existing));
        given(artifactRepository.save(existing)).willReturn(existing);
        //When
        Artifact result = artifactService.update("1250808601744904192", updated);
        //Then
        assertThat(result.getId()).isEqualTo(existing.getId());
        assertThat(result.getName()).isEqualTo(updated.getName());
        assertThat(result.getDescription()).isEqualTo(updated.getDescription());
        assertThat(result.getImgUrl()).isEqualTo(updated.getImgUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
        verify(artifactRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateArtifactNotFound() {
        //Given
        Artifact updated = new Artifact();
        updated.setName("Updated Name");
        updated.setDescription("Updated Description");
        updated.setImgUrl("Updated ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
        //When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.update("1250808601744904192", updated);
        });
        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteArtifactSuccess() {
        //Given
        Artifact existing = new Artifact();
        existing.setId("1250808601744904192");
        existing.setName("Invisibility Cloak");
        existing.setDescription("An invisibility cloak is used to make the wearer invisible.");
        existing.setImgUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(existing));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");
        //When
        artifactService.deleteById("1250808601744904192");
        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
        verify(artifactRepository, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteArtifactNotFound() {
        //Given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
        //When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.deleteById("1250808601744904192");
        });
        //Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

}