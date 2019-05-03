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
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.model.CommentDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.time.LocalDateTime;
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
@WebMvcTest(CommentRestController.class)
@ActiveProfiles("test")
class CommentRestControllerTest {
    @Configuration
    @Import({ CommentRestController.class })
    static class Config {
    }
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentService service;

    private CommentDto comment;

    @BeforeEach
    void setUp() {
        comment = new CommentDto(
                "2c77bb3f57cfe05a39abc17a",
                "Name of commentator",
                LocalDateTime.parse("2019-04-21T16:24:03.353"),
                "message",
                "Book_ID"
                );
    }

    @Test
    void createComment() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/comment/")
                .content(mapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/api/comment/")
                .content(mapper.writeValueAsString(new CommentDto("doesn't matter", null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/comment/")
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not comment))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/comment/")
                .content(new byte[0])
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(1))
                .create(comment.getUsername(), comment.getMessage(), comment.getBookId());
    }

    @Test
    void updateComment() throws Exception {
        mvc.perform(put("/api/comment/"))
                .andExpect(status().isMethodNotAllowed());
        mvc.perform(put("/api/comment/" + comment.getId()))
                .andExpect(status().isBadRequest());

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = mvc.perform(put("/api/comment/" + comment.getId())
                .content(mapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        mvc.perform(put("/api/comment/" + comment.getId())
                .content(mapper.writeValueAsString(new GenreDto("doesn't matter", "not comment))")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(put("/api/comment/" + comment.getId())
                .content(comment.getMessage())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).update(comment.getId(), comment.getMessage());
    }

    @Test
    void getComment() throws Exception {
        when(service.getComment(comment.getId()))
                .thenReturn(comment);

        mvc.perform(get("/api/comment/" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId())))
                .andExpect(jsonPath("$.username", is(comment.getUsername())))
                .andExpect(jsonPath("$.message", is(comment.getMessage())))
                .andExpect(jsonPath("$.created", is(comment.getCreated().toString())))
                .andExpect(jsonPath("$.bookId", is(comment.getBookId())));

        MvcResult mvcResult = mvc.perform(get("/api/comment/" + "just_another_unreal_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).getComment(comment.getId());
    }

    @Test
    void getComments() throws Exception {
        when(service.getAll())
                .thenReturn(Collections.singletonList(comment));

        mvc.perform(get("/api/comment/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(comment.getId())))
                .andExpect(jsonPath("$[0].username", is(comment.getUsername())))
                .andExpect(jsonPath("$[0].message", is(comment.getMessage())))
                .andExpect(jsonPath("$[0].created", is(comment.getCreated().toString())))
                .andExpect(jsonPath("$[0].bookId", is(comment.getBookId())));

        verify(service, times(1)).getAll();
    }

    @Test
    void getBookComments() throws Exception {
        when(service.getCommentsFor(comment.getBookId()))
                .thenReturn(Collections.singletonList(comment));

        mvc.perform(get("/api/comment/book/" + comment.getBookId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(comment.getId())))
                .andExpect(jsonPath("$[0].username", is(comment.getUsername())))
                .andExpect(jsonPath("$[0].message", is(comment.getMessage())))
                .andExpect(jsonPath("$[0].created", is(comment.getCreated().toString())))
                .andExpect(jsonPath("$[0].bookId", is(comment.getBookId())));

        verify(service, times(1)).getCommentsFor(comment.getBookId());
    }

    @Test
    void deleteCommentById() throws Exception {
        mvc.perform(delete("/api/comment/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/comment/" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
}