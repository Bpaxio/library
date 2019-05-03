package ru.otus.bbpax.rest.webflux;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.repository.reactive.CommentReactiveRepo;
import ru.otus.bbpax.service.model.CommentDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@WebFluxTest(CommentRestFluxController.class)
class CommentRestFluxControllerTest {

    @Configuration
    @Import({ CommentRestFluxController.class })
    static class Config {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentReactiveRepo commentReactiveRepository;
    @MockBean
    private BookReactiveRepo bookReactiveRepository;;

    private static final String BASE_URL = "flux/comment/";

    private CommentDto commentDto() {
        return new CommentDto(
                "1",
                "Name of commentator",
                LocalDateTime.parse("2019-04-21T16:24:03.353"),
                "message",
                "Book_ID"
        );
    }

    private Comment comment() {
        return new Comment(
                "1",
                "Name of commentator",
                LocalDateTime.parse("2019-04-21T16:24:03.353"),
                "message",
                book()
        );
    }

    Book book() {
        return new Book(
                "Book_ID",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                new Genre("GenreId", "GenreId"),
                new Author("AuthorId", "AuthorName", "Surname", "country"));
    }

    @Test
    void createComment() {
        CommentDto commentDto = commentDto();
        Comment comment = new Comment(commentDto.getUsername(), commentDto.getMessage(), book());
        when(commentReactiveRepository.save(any())).thenReturn(Mono.just(comment));
        when(bookReactiveRepository.findById(commentDto.getBookId())).thenReturn(Mono.just(book()));

        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus().isOk();

        verify(commentReactiveRepository).save(any());
    }

    @Test
    void editComment() {
        when(commentReactiveRepository.save(comment())).thenReturn(Mono.just(comment()));
        when(commentReactiveRepository.findById(comment().getId())).thenReturn(Mono.just(comment()));

        webTestClient.put().uri(BASE_URL + commentDto().getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto().getMessage()), String.class)
                .exchange()
                .expectStatus().isOk();

        verify(commentReactiveRepository).save(comment());
    }

    @Test
    void getComment() {
        when(commentReactiveRepository.findById(commentDto().getId()))
                .thenReturn(Mono.just(comment()));

        webTestClient.get().uri(BASE_URL + commentDto().getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CommentDto.class)
                .contains(commentDto());
        verify(commentReactiveRepository, times(1))
                .findById(commentDto().getId());
    }

    @Test
    void getBookComments() {

        when(commentReactiveRepository.findAllByBookId(commentDto().getBookId()))
                .thenReturn(Flux.fromStream(Stream.of(comment())));

        webTestClient.get().uri(BASE_URL + "/book/" + commentDto().getBookId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CommentDto.class)
                .contains(commentDto());
        verify(commentReactiveRepository).findAllByBookId(commentDto().getBookId());
    }

    @Test
    void getComments() {
        when(commentReactiveRepository.findAll())
                .thenReturn(Flux.fromStream(Stream.of(comment())));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CommentDto.class)
                .contains(commentDto());
        verify(commentReactiveRepository).findAll();
    }

    @Test
    void deleteComment() {
        when(commentReactiveRepository.deleteById(commentDto().getId()))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri(BASE_URL + commentDto().getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNoContent();

        verify(commentReactiveRepository).deleteById(commentDto().getId());
    }
}