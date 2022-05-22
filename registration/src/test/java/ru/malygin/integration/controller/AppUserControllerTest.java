package ru.malygin.integration.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.malygin.controller.AppUserController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserController appUserController;

    @Test
    public void isNotNull() throws Exception {
        Assertions
                .assertThat(appUserController)
                .isNotNull();
    }

    @Test
    public void statusReturnOk() throws Exception {
        this.mockMvc
                .perform(get("/api/v1/user/status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}