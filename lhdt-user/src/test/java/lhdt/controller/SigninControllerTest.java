package lhdt.controller;

import lhdt.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SigninControllerTest extends BaseControllerTest {

    @Test
    void signin() throws Exception {
        mockMvc.perform(get("/sign/signin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("index"));

        System.out.println(mockMvc);
    }
}