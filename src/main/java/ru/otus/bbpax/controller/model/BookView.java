package ru.otus.bbpax.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookView implements EntityView<Book> {

    private Long id;
    private String name;
    private Integer publicationDate;
    private String publishingOffice;
    private BigDecimal price;

    private String genreName;
    private String authorFullName;

    public static BookView fromEntity(Book book) {
        return Objects.isNull(book)
                ? null
                : new BookView(
                        book.getId(),
                        book.getName(),
                        book.getPublicationDate(),
                        book.getPublishingOffice(),
                        book.getPrice(),
                        book.getGenre().getName(),
                        book.getAuthor().getName() + " " + book.getAuthor().getSurname()
        );
    }

    @Override
    public Book toEntity() {
        return new Book(id,
                name,
                publicationDate,
                publishingOffice,
                price,
                parseGenreView().toEntity(),
                parseAuthorView().toEntity());
    }

    private AuthorView parseAuthorView() {
        String[] split = authorFullName.split(" ");
        // TODO: 2019-01-28 IndexOutOfBoundsException will be thrown
        String name = split[0];
        String surname = split[1];

        return new AuthorView(null, name, surname, null);
    }

    private GenreView parseGenreView() {
        return new GenreView(null, genreName);
    }
}
