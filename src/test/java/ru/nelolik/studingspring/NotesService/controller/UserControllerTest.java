package ru.nelolik.studingspring.NotesService.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.nelolik.studingspring.NotesService.config.SpringConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfig.class)
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void indexMethodTest() throws Exception {
        mockMvc.perform(get("/users")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("users")).
                andExpect(view().name("users/index"));
    }

    @Test
    public void annNewUserShouldRedirect() throws Exception {
        mockMvc.perform(get("/users/new")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users"));
    }

    @Test
    public void manageUsersTest() throws Exception {
        mockMvc.perform(get("/users/manage")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("users")).
                andExpect(view().name("users/manage"));
    }

    @Test
    public void deleteUserShouldRedirect() throws Exception {
        mockMvc.perform(post("/users/manage")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/manage"));
    }

    @Test
    public void showUserTest() throws Exception {
        int id = 1;
        mockMvc.perform(get("/users/{id}", 3L)).
                andExpect(model().attributeExists("user")).
                andExpect(model().attributeExists("notes")).
                andExpect(status().isOk()).
                andExpect(view().name("users/user"));
    }


}
