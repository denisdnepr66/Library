package com.denys.library.some.controllers;


import com.denys.library.some.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        user1 = new User();
        user1.setFirstName("Denys");
        user1.setLastName("Shyshliannykov");
        user1.setEmail("denysdenys@gmail.com");
        user1.setPassword("$2y$12$VXGaW5Ju6e6OM0v9BsB1DO/RIlQulLFQ7Ydn4HfvfCHzTHunQCa.O");

        user2 = new User();
        user2.setFirstName("Denys");
        user2.setLastName("Shyshliannykov");
        user2.setEmail("dasdfenys@gmail.com");
        user2.setPassword("$2y$12$VXGaW5Ju6e6OM0v9BsB1DO/RIlQulLFQ7Ydn4HfvfCHzTHunQCa.O");

//        mockMvc.perform(post("/users/")
//                .with(httpBasic("denys@gmail.com","password"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(user2)));
    }


    @Test
    public void test(){
        assertThat(userController).isNotNull();
    }

    @Test
    public void testListUsers() throws Exception{
        mockMvc.perform(get("/users")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserWithExistingId() throws Exception{
        mockMvc.perform(get("/users/1")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserWithNonExistingId() throws Exception{
        mockMvc.perform(get("/users/123")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testAddUser() throws Exception{

        mockMvc.perform(post("/users/")
                .with(httpBasic("libararian@email.com","password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateExistingUser() throws Exception{
        user2.setLastName("asdf");
        mockMvc.perform(put("/users/2")
                .with(httpBasic("libararian@email.com","password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user2)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateExistingUserWithAlreadyTakenEmail() throws Exception{

        mockMvc.perform(put("/users/2")
                .with(httpBasic("libararian@email.com","password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateNonExistingUser() throws Exception{

        mockMvc.perform(put("/users/123")
                .with(httpBasic("libararian@email.com","password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user1)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteExistingUserById() throws Exception{
        mockMvc.perform(delete("/users/2")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteNonExistingUserById() throws Exception{
        mockMvc.perform(delete("/users/123")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBooksByUserId() throws Exception{
        mockMvc.perform(get("/users/3")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGiveRequestedBookToUser() throws Exception{
        mockMvc.perform(put("/users/giveRequestedBooks/1")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGiveAllRequestedBookToUser() throws Exception{
        mockMvc.perform(put("/users/giveAllRequestedBooks")
                .with(httpBasic("libararian@email.com","password")))
                .andExpect(status().isOk());
    }



    private String toJson(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
}