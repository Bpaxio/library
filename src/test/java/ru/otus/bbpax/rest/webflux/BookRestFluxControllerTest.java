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
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.service.model.BookDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@WebFluxTest(BookRestFluxController.class)
class BookRestFluxControllerTest {

    @Configuration
    @Import({ BookRestFluxController.class })
    static class Config {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookReactiveRepo bookReactiveRepository;

    private static final String BASE_URL = "flux/book/";

    private BookDto bookDto() {
        return new BookDto(
                "1",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                "GenreId",
                "AuthorId");
    }

    Book book() {
        return new Book(
                "1",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                new Genre("GenreId", "GenreId"),
                new Author("AuthorId", "AuthorName", "Surname", "country"));
    }

    List<BookDto> books() {
        return Collections.singletonList(bookDto());
    }

    @Test
    void createBookTest() {
        BookDto book = bookDto();
        when(bookReactiveRepository.save(book.toNewEntity())).thenReturn(Mono.just(book()));
        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), BookDto.class)
                .exchange()
                .expectStatus().isOk();

        verify(bookReactiveRepository).save(book.toNewEntity());
    }

    @Test
    void updateBookTest() {
        BookDto book = bookDto();
        when(bookReactiveRepository.save(book.toEntity()))
                .thenReturn(Mono.just(book()));

        webTestClient.put().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), BookDto.class)
                .exchange()
                .expectStatus().isOk();

        verify(bookReactiveRepository).save(book.toEntity());
    }

    @Test
    void getBookTest() {
        when(bookReactiveRepository.findById(bookDto().getId()))
                .thenReturn(Mono.just(book()));

        webTestClient.get().uri(BASE_URL + bookDto().getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(BookDto.class)
                .contains(bookDto());
        verify(bookReactiveRepository, times(1))
                .findById(bookDto().getId());
    }

    @Test
    void getBooksTest() {
        when(bookReactiveRepository.findAll())
                .thenReturn(Flux.fromStream(books().stream().map(BookDto::toEntity)));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(BookDto.class)
                .contains(bookDto());
        verify(bookReactiveRepository).findAll();
    }

    @Test
    void deleteBookByIdTest() {
        when(bookReactiveRepository.deleteById(bookDto().getId()))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri(BASE_URL + bookDto().getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNoContent();

        verify(bookReactiveRepository).deleteById(bookDto().getId());
    }
}