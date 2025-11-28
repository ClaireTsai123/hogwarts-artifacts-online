package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.artifact.DTO.ArtifactDto;
import com.example.hogwartsartifactsonline.system.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//@WebMvcTest(controllers = ArtifactController.class)
//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        artifacts = new ArrayList<>();
        Artifact a1 = new Artifact();
        a1.setId("1");
        a1.setName("Invisibility Cloak");
        a1.setDescription("A cloak that renders the wearer invisible.");
        a1.setImgUrl("ImgUrl1");
        artifacts.add(a1);
        Artifact a2 = new Artifact();
        a2.setId("2");
        a2.setName("Resurrection Stone");
        a2.setDescription("A stone that can bring back the dead.");
        a2.setImgUrl("ImgUrl2");
        artifacts.add(a2);
        Artifact a3 = new Artifact();
        a3.setId("3");
        a3.setName("Elder Wand");
        a3.setDescription("The most powerful wand in existence.");
        a3.setImgUrl("ImgUrl3");
        artifacts.add(a3);
        Artifact a4 = new Artifact();
        a4.setId("4");
        a4.setName("Philosopher's Stone");
        a4.setDescription("A stone that can turn any metal into pure gold and produce the Elixir of Life.");
        a4.setImgUrl("ImgUrl4");
    }

    @AfterEach
    void tearDown() {
    }

    // @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //Given
        given(artifactService.getById("1")).willReturn(artifacts.get(0));

        //When and Then
        this.mockMvc.perform(get("/api/v1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Invisibility Cloak"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        //Given
        given(artifactService.getById("1")).willThrow(new ArtifactNotFoundException("1"));

        //When and Then
        this.mockMvc.perform(get("/api/v1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found artifact with id: 1."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        //Given
        given(artifactService.getAllArtifacts()).willReturn(artifacts);

        //When and Then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.length()").value(this.artifacts.size()));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                null,
                "Marauder's Map",
                "A magical map that shows every detail of Hogwarts, including the location of people within the castle.",
                "ImgUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("5");
        savedArtifact.setName("Marauder's Map");
        savedArtifact.setDescription("A magical map that shows every detail of Hogwarts, including the location of people within the castle.");
        savedArtifact.setImgUrl("ImgUrl");

        given(artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        // When and Then
        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value("5"))
                .andExpect(jsonPath("$.data.name").value("Marauder's Map"));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904192",
                "Updated Name",
                "Updated Description",
                "Updated ImgUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904192");
        updatedArtifact.setName("Updated Name");
        updatedArtifact.setDescription("Updated Description");
        updatedArtifact.setImgUrl("Updated ImgUrl");

        given(artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);

        // When and Then
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imgUrl").value(updatedArtifact.getImgUrl()));

    }

    @Test
    void testUpdatedArtifactNotFound() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904192",
                "Updated Name",
                "Updated Description",
                "Updated ImgUrl",
                null
        );
        String json = this.objectMapper.writeValueAsString(artifactDto);

        given(artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class)))
                .willThrow(new ArtifactNotFoundException("1250808601744904192"));

        // When and Then
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found artifact with id: 1250808601744904192."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(artifactService).deleteById("1250808601744904192");

        // When and Then
            this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flag").value(true))
                    .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                    .andExpect(jsonPath("$.message").value("Delete Success"))
                    .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        // Given
        Mockito.doThrow(new ArtifactNotFoundException("1250808601744904192"))
                .when(artifactService).deleteById("1250808601744904192");
        // When and Then
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found artifact with id: 1250808601744904192."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}