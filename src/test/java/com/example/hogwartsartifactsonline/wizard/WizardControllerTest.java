package com.example.hogwartsartifactsonline.wizard;

import com.example.hogwartsartifactsonline.artifact.Artifact;
import com.example.hogwartsartifactsonline.system.StatusCode;
import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.example.hogwartsartifactsonline.wizard.DTO.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "api.endpoint.base-url=/api/v1"
})
public class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    WizardService wizardService;

    @Value("${api.endpoint.base-url}")
    String base_url;
    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImgUrl("ImageUrl");
        w1.addArtifact(a1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        wizards = List.of(w1, w2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //Given
        given(wizardService.findWizardById(1)).willReturn(wizards.get(0));

        //When and Then
        this.mockMvc.perform(get(this.base_url + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data.name").value(wizards.get(0).getName()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        //Given
        given(wizardService.findWizardById(1)).willThrow(new ObjectNotFoundException("wizard", 1));

        //When and Then
        this.mockMvc.perform(get(this.base_url + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id: 1."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {

        //Given
        given(wizardService.findAllWizards()).willReturn(wizards);

        //When and Then
        this.mockMvc.perform(get(this.base_url + "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(wizards.size()))
                .andExpect(jsonPath("$.data[0].id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(wizards.get(0).getName()));
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(
                null,
                "Hermione Granger",
                null
        );
        String json = this.objectMapper.writeValueAsString(wizardDto);
        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("Hermione Granger");

        given(wizardService.saveWizard(Mockito.any(Wizard.class))).willReturn(savedWizard);

        // When and Then
        this.mockMvc.perform(post(this.base_url + "/wizards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(
                2,
                "Harry Potter-update",
                null
        );
        String json = this.objectMapper.writeValueAsString(wizardDto);
        Wizard updatedWizard = new Wizard();
        updatedWizard.setName("Harry Potter-update");

        given(wizardService.updateWizard(Mockito.eq(2), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and Then
        this.mockMvc.perform(put(this.base_url + "/wizards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updatedWizard.getId()))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testUpdateWizardNotFound() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(
                2,
                "Harry Potter-update",
                null
        );
        String json = this.objectMapper.writeValueAsString(wizardDto);

        given(wizardService.updateWizard(Mockito.eq(2), Mockito.any(Wizard.class)))
                .willThrow(new ObjectNotFoundException("wizard", 2));

        // When and Then
        this.mockMvc.perform(put(this.base_url + "/wizards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id: 2."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(wizardService).deleteWizard(3);
        // When and Then
        this.mockMvc.perform(delete(this.base_url + "/wizards/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        // Given
        Mockito.doThrow(new ObjectNotFoundException("wizard", 3)).when(wizardService).deleteWizard(3);
        // When and Then
        this.mockMvc.perform(delete(this.base_url + "/wizards/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id: 3."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactToWizardSuccess() throws Exception {
        // Given
        Mockito.doNothing().when(wizardService).addArtifactToWizard(2, "1250808601744904192");
        // When and Then
        this.mockMvc.perform(put(this.base_url + "/wizards/2/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactNotFound() throws Exception {
        // Given
        Mockito.doThrow(new ObjectNotFoundException("artifact", "1250808601744904192"))
                .when(wizardService).addArtifactToWizard(2, "1250808601744904192");
        // When and Then
        this.mockMvc.perform(put(this.base_url + "/wizards/2/artifacts/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id: 1250808601744904192."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
