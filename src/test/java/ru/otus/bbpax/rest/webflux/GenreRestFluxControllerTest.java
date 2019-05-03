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
import ru.otus.bbpax.repository.reactive.GenreReactiveRepo;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@WebFluxTest(GenreRestFluxController.class)
class GenreRestFluxControllerTest {

    @Configuration
    @Import({ GenreRestFluxController.class })
    static class Config {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreReactiveRepo genreReactiveRepository;
    @MockBean
    private BookReactiveRepo bookReactiveRepository;

    private static final String BASE_URL = "flux/genre/";

    Genre genre() {
        return new Genre("1", "Novel");
    }

    List<Genre> genres() {
        return Collections.singletonList(genre());
    }

    Book book() {
        return new Book(
                "1",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                new Genre("1", "Novel"),
                new Author("1", "AuthorName", "Surname", "country"));

    }

    List<Book> books() {
        return Collections.singletonList(book());
    }

    @Test
    void getGenreTest() {
        when(genreReactiveRepository.findById(genre().getId()))
                .thenReturn(Mono.just(genre()));

        webTestClient.get().uri(BASE_URL + genre().getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Genre.class)
                .contains(genre());
        verify(genreReactiveRepository, times(1)).findById(genre().getId());
    }

    @Test
    void getGenresTest() {
        when(genreReactiveRepository.findAll())
                .thenReturn(Flux.fromStream(genres().stream()));

        webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Genre.class)
                .contains(genre());
        verify(genreReactiveRepository).findAll();
    }

    @Test
    void getBooksByGenreTest() {
        when(bookReactiveRepository.findAllByGenreId(genre().getId()))
                .thenReturn(Flux.fromStream(books().stream()));

        webTestClient.get().uri(BASE_URL + genre().getId() + "/book")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(BookDto.class)
                .contains(BookDto.fromEntity(book()));
        verify(bookReactiveRepository).findAllByGenreId(genre().getId());
    }

    @Test
    void createGenreTest() {
        Genre newGenre = new Genre("Roman");
        when(genreReactiveRepository.save(newGenre)).thenReturn(Mono.just(newGenre));

        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newGenre.getName()), String.class)
                .exchange()
                .expectStatus().isOk();

        verify(genreReactiveRepository).save(newGenre);
    }

    @Test
    void updateGenreTest() {
        Genre newGenre = new Genre("1", "Biopic");
        when(genreReactiveRepository.save(newGenre)).thenReturn(Mono.just(newGenre));

        webTestClient.put().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(GenreDto.fromEntity(newGenre)), GenreDto.class)
                .exchange()
                .expectStatus().isOk();

        verify(genreReactiveRepository).save(newGenre);
    }

    @Test
    void deleteGenreTest() {
        when(genreReactiveRepository.deleteById(genre().getId()))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri(BASE_URL + genre().getId())
                .exchange()
                .expectStatus().isNoContent();

        verify(genreReactiveRepository).deleteById(genre().getId());
    }
}
