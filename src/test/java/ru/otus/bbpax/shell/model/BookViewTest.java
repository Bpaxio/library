package ru.otus.bbpax.shell.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.service.model.BookView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.shell.model.TestVariables.AUTHOR_NAME;
import static ru.otus.bbpax.shell.model.TestVariables.AUTHOR_SURNAME;
import static ru.otus.bbpax.shell.model.TestVariables.BOOK_ID;
import static ru.otus.bbpax.shell.model.TestVariables.BOOK_NAME;
import static ru.otus.bbpax.shell.model.TestVariables.BOOK_PRICE;
import static ru.otus.bbpax.shell.model.TestVariables.BOOK_PUBLICATION_YEAR;
import static ru.otus.bbpax.shell.model.TestVariables.BOOK_PUBLISHING_OFFICE;
import static ru.otus.bbpax.shell.model.TestVariables.GENRE_NAME;

class BookViewTest {
    private BookView bookView;
    private Book book;

    @BeforeEach
    void setUp() {
        bookView = new BookView(BOOK_ID,
                BOOK_NAME,
                BOOK_PUBLICATION_YEAR,
                BOOK_PUBLISHING_OFFICE,
                BOOK_PRICE,
                GENRE_NAME,
                AUTHOR_NAME,
                AUTHOR_SURNAME);

        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setSurname(AUTHOR_SURNAME);
        Genre genre = new Genre();
        genre.setName(GENRE_NAME);
        book = new Book(BOOK_ID,
                BOOK_NAME,
                BOOK_PUBLICATION_YEAR,
                BOOK_PUBLISHING_OFFICE,
                BOOK_PRICE,
                genre,
                author);
    }

    @Test
    @DisplayName("создает view с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(bookView, BookView.fromEntity(book));
        assertNull(BookView.fromEntity(null));
    }

    @Test
    @DisplayName("создает entity с теми же значениями полей, что у view.")
    void toEntity() {
        assertEquals(book, bookView.toEntity());
    }
}