package ru.otus.bbpax.rest;

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
@WebMvcTest(GenreRestController.class)
@ActiveProfiles("test")
class GenreRestControllerTest {

    @Configuration
    @Import({ GenreRestController.class })
    static class Config {
    }
    @Autowired
    private MockMvc mvc;
    @MockBean
    private GenreService service;

    private GenreDto genre;

    @BeforeEach
    void setUp() {
        genre = new GenreDto("2c77bb3f57cfe05a39abc17a","Novel");
    }

    @Test
    void createGenre() throws Exception {
        mvc.perform(post("/api/genre/")
                .content(genre.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1))
                .create(new GenreDto(null, genre.getName()));
    }

    @Test
    void updateGenre() throws Exception {

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
    void getGenre() throws Exception {
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
    void getGenres() throws Exception {
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
    void getBooks() throws Exception {

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
    void deleteGenreById() throws Exception {
        mvc.perform(delete("/api/genre/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/genre/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(genre.getId());
    }
}