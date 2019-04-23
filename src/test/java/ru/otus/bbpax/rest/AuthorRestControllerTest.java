package ru.otus.bbpax.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vlad Rakhlinskii
 * Created on 18.04.2019.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorRestController.class) // TODO: 2019-04-18 why this doesn't work?((((
@ActiveProfiles("test")
class AuthorRestControllerTest {
    @Configuration
    @Import({ AuthorRestController.class })
    static class Config {
    }
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthorService service;

    private AuthorDto author;

    @BeforeEach
    void setUp() {
        author = new AuthorDto("2c77bb3f57cfe05a39abc17a","Name", "Surname", "Country");
    }

    @Test
    void createAuthor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/author/")
                .content(mapper.writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/api/author/")
                .content(mapper.writeValueAsString(new AuthorDto("doesn't matter", null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/author/")
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not author))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/author/")
                .content(new byte[0])
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).create(author);
    }

    @Test
    void updateAuthor() throws Exception {
        mvc.perform(put("/api/author/" + author.getId()))
                .andExpect(status().isMethodNotAllowed());
        mvc.perform(put("/api/author/"))
                .andExpect(status().isBadRequest());

        MvcResult mvcResult = mvc.perform(put("/api/author/")
                .content(author.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(put("/api/author/")
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not author))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(put("/api/author/")
                .content(mapper.writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).update(author);
    }

    @Test
    void getAuthor() throws Exception {
        when(service.getAuthorById(author.getId()))
                .thenReturn(author);

        mvc.perform(get("/api/author/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(author.getId())))
                .andExpect(jsonPath("$.name", is(author.getName())))
                .andExpect(jsonPath("$.surname", is(author.getSurname())))
                .andExpect(jsonPath("$.country", is(author.getCountry())));

        MvcResult mvcResult = mvc.perform(get("/api/author/" + "just_another_unreal_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).getAuthorById(author.getId());
    }

    @Test
    void getAuthors() throws Exception {
        when(service.getAll())
                .thenReturn(Collections.singletonList(author));

        mvc.perform(get("/api/author/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(author.getId())))
                .andExpect(jsonPath("$[0].name", is(author.getName())))
                .andExpect(jsonPath("$[0].surname", is(author.getSurname())))
                .andExpect(jsonPath("$[0].country", is(author.getCountry())));;


        verify(service, times(1)).getAll();
    }

    @Test
    void deleteAuthorById() throws Exception {
        mvc.perform(delete("/api/author/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/author/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(author.getId());
    }
}