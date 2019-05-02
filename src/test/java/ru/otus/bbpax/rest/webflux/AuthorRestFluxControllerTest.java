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
import ru.otus.bbpax.repository.reactive.AuthorReactiveRepo;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@WebFluxTest(AuthorRestFluxController.class)
class AuthorRestFluxControllerTest {

    @Configuration
    @Import({ AuthorRestFluxController.class })
    static class Config {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorReactiveRepo authorReactiveRepository;
    @MockBean
    private BookReactiveRepo bookReactiveRepository;

    private static final String BASE_URL = "flux/author/";

    Author author() {
        return new Author("1", "John", "Snow", "Winterfell");
    }

    List<Author> authors() {
        return Collections.singletonList(author());
    }

    Book book() {
        return new Book(
                "1",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                new Genre("1", "GenreId"),
                new Author("1", "AuthorName", "Surname", "country"));
    }

    List<Book> books() {
        return Collections.singletonList(book());
    }

    @Test
    void authorTest() throws Exception {
        when(authorReactiveRepository.findById(author().getId()))
                .thenReturn(Mono.just(author()));

        webTestClient.get().uri(BASE_URL + author().getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Author.class)
                .contains(author());
        verify(authorReactiveRepository, times(1))
                .findById(author().getId());
    }

    @Test
    void authorsTest() throws Exception {
        when(authorReactiveRepository.findAll())
                .thenReturn(Flux.fromStream(authors().stream()));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Author.class)
                .contains(author());
        verify(authorReactiveRepository).findAll();
    }

    @Test
    void getBooksByAuthorTest() throws Exception {
        when(bookReactiveRepository.findAllByAuthorId(author().getId()))
                .thenReturn(Flux.fromStream(books().stream()));

        webTestClient.get().uri(BASE_URL + author().getId() + "/book/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Book.class)
                .contains(book());
        verify(bookReactiveRepository).findAllByAuthorId(author().getId());
    }

    @Test
    void createAuthorTest() throws Exception {
        Author newAuthor = new Author("Another", "Man", "Russia");
        when(authorReactiveRepository.save(newAuthor)).thenReturn(Mono.just(newAuthor));
        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newAuthor), Author.class)
                .exchange()
                .expectStatus().isOk();

        verify(authorReactiveRepository).save(newAuthor);
    }

    @Test
    void updateAuthorTest() throws Exception {
        Author newAuthor = new Author("1", "Some", "One", "Russia");
        when(authorReactiveRepository.save(newAuthor)).thenReturn(Mono.just(newAuthor));

        webTestClient.put().uri(BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newAuthor), Author.class)
                .exchange()
                .expectStatus().isOk();

        verify(authorReactiveRepository).save(newAuthor);
    }

    @Test
    void deleteAuthorTest() throws Exception {
        when(authorReactiveRepository.deleteById(author().getId()))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri(BASE_URL + author().getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNoContent();

        verify(authorReactiveRepository).deleteById(author().getId());
    }
}
