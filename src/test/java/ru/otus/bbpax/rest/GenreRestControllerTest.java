package ru.otus.bbpax.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreDto;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vlad Rakhlinskii
 * Created on 18.04.2019.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreRestController.class)
@ComponentScan(basePackageClasses = MvcConfig.class)
class GenreRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    GenreService service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createGenre() {

    }

    @Test
    void updateGenre() {
    }

    @Test
    void getGenre() throws Exception {
        String id = "2c77bb3f57cfe05a39abc17a";
        GenreDto genre = new GenreDto("2c77bb3f57cfe05a39abc17a","Novel");
        given(service.getGenreById(id)).willReturn(genre);

        mvc.perform(get("/api/genre/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].name", is(genre.getName())));
    }

    @Test
    void getGenres() {
    }

    @Test
    void deleteGenreById() {
    }
}