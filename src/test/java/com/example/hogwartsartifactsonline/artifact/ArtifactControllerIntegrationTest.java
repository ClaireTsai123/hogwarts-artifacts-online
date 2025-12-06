package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.artifact.DTO.ArtifactDto;
import com.example.hogwartsartifactsonline.system.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Artifact API endpoints")
@Tag("integration")
@TestPropertySource(properties = {
        "api.endpoint.base-url=/api/v1"
})
public class ArtifactControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(this.baseUrl + "/users/login").with(httpBasic("John", "123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllArtifactsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        ArtifactDto a = new ArtifactDto(
                null,
                "Remebrall",
                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something",
                "ImageUrl",
                null
        );


        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts").header("Authorization", this.token).contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Remebrall"))
                .andExpect(jsonPath("$.data.description").value("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
        this.mockMvc.perform(get(this.baseUrl + "/artifacts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(7)));

    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.data.description").value("An invisibility cloak is used to make the wearer invisible."))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));

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

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/1250808601744904192").header("Authorization", this.token).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"))
                .andExpect(jsonPath("$.data.imageUrl").value("Updated ImgUrl"));

    }

    @Test
    void testDeleteSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/1250808601744904192").header("Authorization", this.token).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl+ "/artifacts/1250808601744904199").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id: 1250808601744904199."))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
