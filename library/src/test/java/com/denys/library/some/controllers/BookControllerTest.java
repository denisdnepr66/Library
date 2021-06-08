package com.denys.library.some.controllers;

import com.denys.library.some.models.Book;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private BookController bookController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Book bookToCreate;
    private Book bookToDelete;
    private Book bookToUpdate;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        bookToCreate = new Book();
        bookToCreate.setTitle("bookToCreateTitle");
        bookToCreate.setAuthor("bookToCreateAuthor");
        bookToCreate.setShortDescription("bookToCreateShortDescription");
        bookToCreate.setYearPublished(1999);

        bookToDelete = new Book();
        bookToDelete.setTitle("bookTDeleteTitle");
        bookToDelete.setAuthor("bookToDeleteAuthor");
        bookToDelete.setShortDescription("bookToDeleteShortDescription");
        bookToDelete.setYearPublished(1999);

        bookToUpdate = new Book();
        bookToUpdate.setTitle("bookToUpdateTitle");
        bookToUpdate.setAuthor("bookToUpdateAuthor");
        bookToUpdate.setShortDescription("bookToUpdateShortDescription");
        bookToUpdate.setYearPublished(1999);


    }

    @Test
    public void test(){
        assertThat(bookController).isNotNull();
    }

    @Test
    public void listBooks() throws Exception{
        mockMvc.perform(get("/books")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findBookById() throws Exception {
        mockMvc.perform(get("/books/1")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }
    @Test
    public void findBookByNonExistingId() throws Exception {
        mockMvc.perform(get("/books/1123")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addBook() throws Exception{
        mockMvc.perform(post("/books/")
                .with(httpBasic("libararian@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(bookToCreate)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateBook() throws Exception{
        mockMvc.perform(post("/books/1")
                .with(httpBasic("libararian@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(bookToUpdate)))
                .andExpect(status().isOk());
    }
    @Test
    public void updateBookByNonExistingId() throws Exception{
        mockMvc.perform(post("/books/1234")
                .with(httpBasic("libararian@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(bookToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBookById() throws Exception{
        mockMvc.perform(get("/books/2")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByTitle() throws Exception{
        mockMvc.perform(get("/books/title=bookToCreateTitle")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByNonExistingTitle() throws Exception{
        mockMvc.perform(get("/books/title=bookToCreateTitle")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByAuthorFullName() throws Exception{
        mockMvc.perform(get("/books/author=bookToCreateAuthor")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByAuthorPartName() throws Exception{
        mockMvc.perform(get("/books/author=bookToCreateA")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByYearPublished() throws Exception{
        mockMvc.perform(get("/books/yearPublished=1999")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByFullShortDescription() throws Exception{
        mockMvc.perform(get("/books/shortDescription=bookToCreateShortDescription")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void findByPartShortDescription() throws Exception{
        mockMvc.perform(get("/books/shortDescription=bookToCreateShortDes")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void getAvailableBooks() throws Exception{
        mockMvc.perform(get("/books/available")
                .with(httpBasic("libararian@email.com", "password")))
                .andExpect(status().isOk());
    }

    private String toJson(Book book) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(book);
    }
}