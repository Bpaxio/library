package ru.otus.bbpax.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.error.NotFoundException;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.math.BigDecimal;
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
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(BookRestController.class)
@ActiveProfiles("test")
class BookRestControllerTest {
    @Configuration
    @Import({ BookRestController.class })
    static class Config {
    }
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService service;

    private BookDto book;

    @BeforeEach
    void setUp() {
        book = new BookDto(
                "2c77bb3f57cfe05a39abc17a",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                "Genre))",
                "Author",
                "Tester");
    }

    @Test
    void createBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(book));
        mvc.perform(post("/api/book/")
                .content(mapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/api/book/")
                .content(mapper.writeValueAsString(
                        new BookDto("doesn't matter",
                                null,
                                null,
                                null,
                                null,
                                null,
                                null)
                        )
                )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/book/")
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not book))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/book/")
                .content(new byte[0])
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).create(book);
    }

    @Test
    void updateBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        log.info(mapper.writeValueAsString(book));
        mvc.perform(put("/api/book/")
                .content(mapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(put("/api/book/")
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not book))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(put("/api/book/" + book.getId()))
                .andExpect(status().isMethodNotAllowed());
        mvc.perform(put("/api/book/"))
                .andExpect(status().isBadRequest());

        MvcResult mvcResult = mvc.perform(put("/api/book/")
                .content(book.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());


        verify(service, times(1)).update(book);
    }

    @Test
    void getBook() throws Exception {
        when(service.getBookById("just_another_unreal_id")).thenThrow(NotFoundException.class);

        when(service.getBookById(book.getId()))
                .thenReturn(book);

        mvc.perform(get("/api/book/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> log.info("{}", result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id", is(book.getId())))
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.price", is(book.getPrice().toString())))
                .andExpect(jsonPath("$.publicationDate", is(book.getPublicationDate())))
                .andExpect(jsonPath("$.publishingOffice", is(book.getPublishingOffice())))
                .andExpect(jsonPath("$.genreName", is(book.getGenreName())))
                .andExpect(jsonPath("$.authorFirstName", is(book.getAuthorFirstName())))
                .andExpect(jsonPath("$.authorLastName", is(book.getAuthorLastName())))
                .andReturn();

        MvcResult mvcResult = mvc.perform(get("/api/book/" + "just_another_unreal_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1))
                .getBookById(book.getId());
    }

    @Test
    void getBooks() throws Exception {
        when(service.getAll())
                .thenReturn(Collections.singletonList(book));

        mvc.perform(get("/api/book/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(book.getId())))
                .andExpect(jsonPath("$[0].name", is(book.getName())))
                .andExpect(jsonPath("$[0].price", is(book.getPrice().toString())))
                .andExpect(jsonPath("$[0].publicationDate", is(book.getPublicationDate())))
                .andExpect(jsonPath("$[0].publishingOffice", is(book.getPublishingOffice())))
                .andExpect(jsonPath("$[0].genreName", is(book.getGenreName())))
                .andExpect(jsonPath("$[0].authorFirstName", is(book.getAuthorFirstName())))
                .andExpect(jsonPath("$[0].authorLastName", is(book.getAuthorLastName())));


        verify(service, times(1)).getAll();
    }

    @Test
    void deleteBookById() throws Exception {
        mvc.perform(delete("/api/book/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(book.getId());

        mvc.perform(delete("/api/book/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}