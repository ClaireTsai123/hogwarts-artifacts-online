package com.example.hogwartsartifactsonline.wizard;

import com.example.hogwartsartifactsonline.system.StatusCode;
import com.example.hogwartsartifactsonline.wizard.DTO.WizardDto;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Wizard API endpoints")
@Tag("integration")
@TestPropertySource(properties = {
        "api.endpoint.base-url=/api/v1"
})
public class WizardControllerIntegrationTest {

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
    void testFindAllWizardsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/wizards").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));

    }

    @Test
    void testAddWizardSuccess() throws Exception {
        WizardDto w = new WizardDto(
                null,
                "Hermione Granger",
                null
        );

        String json = objectMapper.writeValueAsString(w);
        this.mockMvc.perform(post(this.baseUrl + "/wizards").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Hermione Granger"));
        this.mockMvc.perform(get(this.baseUrl + "/wizards").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));

    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/wizards/1").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testUpdateWizardSuccess() throws  Exception {
        WizardDto wizardDto = new WizardDto(
                2,
                "Harry Potter-update",
                null
        );
        String json = this.objectMapper.writeValueAsString(wizardDto);
        this.mockMvc.perform(put(this.baseUrl +"/wizards/2").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("Harry Potter-update"));

    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        this.mockMvc.perform(delete(this.baseUrl +"/wizards/2").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(get(this.baseUrl + "/wizards").header("Authorization", this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)));

    }
}
