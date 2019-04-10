package ru.otus.bbpax.configuration.changelog.data.builder;

import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookDataBuilder implements DataBuilder<Book> {
    private final List<Book> books;

    private Genre novel;
    private Genre drama;
    private Author authorTest;
    private Author author2;

    public BookDataBuilder() {
        books = new ArrayList<>();
        novel = null;
        drama = null;
        authorTest = null;
        author2 = null;
    }

    @Override
    public DataBuilder<Book> defaultData() {
        Book book = new Book("1c77bb3f57cfe05a39abc17a",
                "Novel of AuthorTest",
                1999,
                "testOffice",
                BigDecimal.valueOf(999.99),
                novel,
                author2,
                null);
        books.add(book);

        book = new Book("2c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                2000,
                "testOffice",
                BigDecimal.valueOf(959.99),
                drama,
                authorTest,
                null);
        books.add(book);

        book = new Book("3c77bb3f57cfe05a39abc17a",
                "Again Novel of AuthorTest",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                novel,
                authorTest,
                null);
        books.add(book);

        book = new Book("4c77bb3f57cfe05a39abc17a",
                "Science fiction of AuthorTest",
                1997,
                "testOffice",
                BigDecimal.valueOf(859.99),
                drama,
                authorTest,
                null);
        books.add(book);

        book = new Book("5c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                1996,
                "testOffice",
                BigDecimal.valueOf(799.99),
                novel,
                authorTest,
                null);
        books.add(book);
        return this;
    }

    @Override
    public DataBuilder<Book> with(Book entity) {
        books.add(entity);
        return this;
    }

    @Override
    public List<Book> build() {
        return books;
    }

    public BookDataBuilder withNovel(Genre novel) {
        this.novel = novel;
        return this;
    }

    public BookDataBuilder withDrama(Genre drama) {
        this.drama = drama;
        return this;
    }

    public BookDataBuilder withTestAuthor(Author author) {
        this.authorTest = author;
        return this;
    }

    public BookDataBuilder withAuthor2(Author author) {
        this.author2 = author;
        return this;
    }
}
