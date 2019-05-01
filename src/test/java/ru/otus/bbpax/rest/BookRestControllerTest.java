package ru.otus.bbpax.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ru.otus.bbpax.configuration.SecurityConfig;
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
import static ru.otus.bbpax.entity.security.Roles.ADMIN;

/**
 * @author Vlad Rakhlinskii
 * Created on 18.04.2019.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(BookRestController.class)
@WithMockUser
@ActiveProfiles("test")
class BookRestControllerTest {
    @Configuration
    @Import({ BookRestController.class, SecurityConfig.class })
    static class Config {
        @MockBean
        @Qualifier("customUserDetailsService")
        public UserDetailsService userDetailsService;
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService service;

    private BookDto book() {
        return new BookDto(
                "2c77bb3f57cfe05a39abc17a",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                "GenreId",
                "AuthorId");
    }

    @Test
    @WithMockUser(roles = {ADMIN})
    void createBook() throws Exception {
        BookDto book = book();
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
    void createBookWithUser() throws Exception {
        BookDto book = book();
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(book));
        mvc.perform(post("/api/book/")
                .content(mapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).create(book);
    }

    @Test
    @WithMockUser(roles = {ADMIN})
    void updateBook() throws Exception {
        BookDto book = book();
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
    void updateBookWithUser() throws Exception {
        BookDto book = book();
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(book));
        mvc.perform(put("/api/book/")
                .content(mapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).update(book);
    }

    @Test
    @WithMockUser(roles = {ADMIN})
    void getBook() throws Exception {
        BookDto book = book();
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
                .andExpect(jsonPath("$.genreId", is(book.getGenreId())))
                .andExpect(jsonPath("$.authorId", is(book.getAuthorId())))
                .andReturn();

        MvcResult mvcResult = mvc.perform(get("/api/book/" + "just_another_unreal_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1))
                .getBookById(book.getId());
    }

    @Test
    void getBookWithUser() throws Exception {
        getBook();
    }

    @Test
    @WithMockUser(roles = {ADMIN})
    void getBooks() throws Exception {
        BookDto book = book();
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
                .andExpect(jsonPath("$[0].genreId", is(book.getGenreId())))
                .andExpect(jsonPath("$[0].authorId", is(book.getAuthorId())));


        verify(service, times(1)).getAll();
    }

    @Test
    void getBooksWithUser() throws Exception {
        getBooks();
    }

    @Test
    @WithMockUser(roles = {ADMIN})
    void deleteBookById() throws Exception {
        BookDto book = book();
        mvc.perform(delete("/api/book/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(book.getId());

        mvc.perform(delete("/api/book/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteBookByIdWithUser() throws Exception {
        BookDto book = book();
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(book));
        mvc.perform(delete("/api/book/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(service, times(0)).deleteById(book.getId());
    }

}