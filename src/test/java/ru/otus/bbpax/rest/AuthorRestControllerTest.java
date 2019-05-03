package ru.otus.bbpax.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.bbpax.configuration.security.SecurityConfig;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorRestController.class)
@WithMockUser
@ActiveProfiles("test")
class AuthorRestControllerTest {
    @Configuration
    @Import({ AuthorRestController.class, SecurityConfig.class })
    static class Config {
        @MockBean
        @Qualifier("customUserDetailsService")
        public UserDetailsService userDetailsService;
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthorService service;

    private AuthorDto author() {
        return new AuthorDto("2c77bb3f57cfe05a39abc17a","Name", "Surname", "Country");
    }

    @Test
    @WithMockAdmin
    void createAuthor() throws Exception {
        AuthorDto author = author();

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
    @Disabled
    void createAuthorWithUser() throws Exception {
        AuthorDto author = author();
        mvc.perform(post("/api/author/")
                .content(new ObjectMapper().writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).create(author);
    }

    @Test
    @WithMockAdmin
    void updateAuthor() throws Exception {
        AuthorDto author = author();
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
    @Disabled
    void updateAuthorWithUser() throws Exception {
        AuthorDto author = author();
        mvc.perform(put("/api/author/")
                .content(new ObjectMapper().writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).update(author);
    }

    @Test
    @WithMockAdmin
    void getAuthor() throws Exception {
        AuthorDto author = author();
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
    void getAuthorWithUser() throws Exception {
        getAuthor();
    }

    @Test
    @WithMockAdmin
    void getAuthors() throws Exception {
        AuthorDto author = author();
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
    void getAuthorsWithUser() throws Exception {
        getAuthors();
    }

    @Test
    @WithMockAdmin
    void getBooks() throws Exception {
        AuthorDto author = author();
        when(service.getBooksById(anyString()))
                .thenReturn(Collections.emptyList());
        BookDto book = new BookDto("id",
                "book",
                2011,
                "office",
                BigDecimal.valueOf(500),
                "BEstId",
                "authorId"
        );
        when(service.getBooksById(author.getId()))
                .thenReturn(Collections.singletonList(book));



        mvc.perform(get("/api/author/someId/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mvc.perform(get("/api/author/" + author.getId() + "/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(book.getId())))
                .andExpect(jsonPath("$[0].name", is(book.getName())))
                .andExpect(jsonPath("$[0].price", is(book.getPrice().toString())))
                .andExpect(jsonPath("$[0].publicationDate", is(book.getPublicationDate())))
                .andExpect(jsonPath("$[0].publishingOffice", is(book.getPublishingOffice())))
                .andExpect(jsonPath("$[0].genreId", is(book.getGenreId())))
                .andExpect(jsonPath("$[0].authorId", is(book.getAuthorId())));


        verify(service, times(1)).getBooksById(author.getId());
    }

    @Test
    void getBooksWithUser() throws Exception {
        getBooks();
    }

    @Test
    @WithMockAdmin
    void deleteAuthorById() throws Exception {
        AuthorDto author = author();
        mvc.perform(delete("/api/author/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/author/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(author.getId());
    }

    @Test
    @Disabled
    void deleteAuthorByIdWithUser() throws Exception {
        AuthorDto author = author();
        mvc.perform(delete("/api/author/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).deleteById(author.getId());
    }
}