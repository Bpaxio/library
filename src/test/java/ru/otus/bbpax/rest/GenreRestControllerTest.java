package ru.otus.bbpax.rest;

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
import ru.otus.bbpax.service.GenreService;
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
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GenreRestController.class)
@WithMockUser
@ActiveProfiles("test")
class GenreRestControllerTest {
    @Configuration
    @Import({ GenreRestController.class, SecurityConfig.class })
    static class Config {
        @MockBean
        @Qualifier("customUserDetailsService")
        public UserDetailsService userDetailsService;
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private GenreService service;

    private GenreDto genre() {
        return new GenreDto("2c77bb3f57cfe05a39abc17a","Novel");
    }

    @Test
    @WithMockAdmin
    void createGenre() throws Exception {
        GenreDto genre = genre();
        mvc.perform(post("/api/genre/")
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1))
                .create(new GenreDto(null, genre.getName()));
    }

    @Test
    @Disabled
    void createGenreWithUser() throws Exception {
        GenreDto genre = genre();
        genre.setId(null);
        mvc.perform(post("/api/genre/")
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0))
                .create(genre);
    }

    @Test
    @WithMockAdmin
    void updateGenre() throws Exception {
        GenreDto genre = genre();

        mvc.perform(put("/api/genre/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        MvcResult mvcResult = mvc.perform(put("/api/genre/" + genre.getId())
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).update(genre);
    }

    @Test
    @Disabled
    void updateGenreWithUser() throws Exception {
        GenreDto genre = genre();
        mvc.perform(put("/api/genre/" + genre.getId())
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0))
                .update(genre);
    }

    @Test
    @WithMockAdmin
    void getGenre() throws Exception {
        GenreDto genre = genre();
        when(service.getGenreById(genre.getId()))
                .thenReturn(genre);

        mvc.perform(get("/api/genre/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(genre.getId())))
                .andExpect(jsonPath("$.name", is(genre.getName())));

        verify(service, times(1)).getGenreById(genre.getId());
    }

    @Test
    void getGenreWithUser() throws Exception {
        getGenre();
    }

    @Test
    @WithMockAdmin
    void getGenres() throws Exception {
        GenreDto genre = genre();
        when(service.getAll())
                .thenReturn(Collections.singletonList(genre));

        mvc.perform(get("/api/genre/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(genre.getId())))
                .andExpect(jsonPath("$[0].name", is(genre.getName())));


        verify(service, times(1)).getAll();
    }

    @Test
    void getGenresWithUser() throws Exception {
        getGenres();
    }

    @Test
    @WithMockAdmin
    void getBooks() throws Exception {
        GenreDto genre = genre();

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
        when(service.getBooksById(genre.getId()))
                .thenReturn(Collections.singletonList(book));



        mvc.perform(get("/api/genre/someId/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mvc.perform(get("/api/genre/" + genre.getId() + "/book")
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


        verify(service, times(1)).getBooksById(genre.getId());
    }

    @Test
    void getBooksWithUser() throws Exception {
        getBooks();
    }

    @Test
    @WithMockAdmin
    void deleteGenreById() throws Exception {
        GenreDto genre = genre();
        mvc.perform(delete("/api/genre/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/genre/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(genre.getId());
    }

    @Test
    @Disabled
    void deleteGenreByIdWithUser() throws Exception {
        GenreDto genre = genre();
        mvc.perform(delete("/api/genre/" + genre.getId())
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0))
                .deleteById(genre.getId());
    }
}