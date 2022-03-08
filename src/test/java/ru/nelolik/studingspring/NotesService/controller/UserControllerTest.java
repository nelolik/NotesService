package ru.nelolik.studingspring.NotesService.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.nelolik.studingspring.NotesService.config.TestContainerConfig;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = {TestContainerConfig.class, UsersController.class})
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void indexMethodTest() throws Exception {

        mockMvc.perform(get("/users")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("users")).
                andExpect(view().name("users/index"));
    }

    @Test
    public void addNewUserShouldRedirect() throws Exception {
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
        mockMvc.perform(post("/users/manage/delete")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/manage"));
    }

    @Test
    public void editUserTestShouldRedirect() throws Exception {
        mockMvc.perform(post("/users/manage/edit")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/manage"));
    }

    @Test
    public void showUserTest() throws Exception {
        int id = 1;
        mockMvc.perform(get("/users/{id}", id)).
                andExpect(model().attributeExists("user")).
                andExpect(model().attributeExists("notes")).
                andExpect(status().isOk()).
                andExpect(view().name("users/user"));
    }

    @Test
    public void showUserJsonTest() throws Exception {
        int id = 1;
        mockMvc.perform(get("/users/{id}/json", id)).
                andExpect(content().contentType(MediaType.valueOf("application/json"))).
                andExpect(content().string(containsString("user"))).
                andExpect(content().string(containsString("notes"))).
                andExpect(content().string(containsString("username"))).
                andExpect(content().string(containsString("password"))).
                andExpect(content().string(containsString("roles")));
    }

    @Test
    public void getAllUsersJsonTest() throws Exception {
        mockMvc.perform(get("/users/json")).
                andExpect(content().contentType(MediaType.valueOf("application/json"))).
                andExpect(content().string(containsString("id"))).
                andExpect(content().string(containsString("username"))).
                andExpect(content().string(containsString("password"))).
                andExpect(content().string(containsString("roles")));
    }

}
