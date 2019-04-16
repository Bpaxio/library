package ru.otus.bbpax.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.entity.Book;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookView implements EntityView<Book> {

    private String id;
    private String name;
    private Integer publicationDate;
    private String publishingOffice;
    private BigDecimal price;

    private String genreName;
    private String authorFirstName;
    private String authorLastName;

    public BookView(String name,
                    Integer publicationDate,
                    String publishingOffice,
                    BigDecimal price,
                    String genreName,
                    String authorFirstName,
                    String authorLastName
    ) {
        this.name = name;
        this.publicationDate = publicationDate;
        this.publishingOffice = publishingOffice;
        this.price = price;
        this.genreName = genreName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }

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
                        book.getAuthor().getName(),
                        book.getAuthor().getSurname()
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

    public String getAuthorFullName() {
        return authorFirstName + (Objects.nonNull(authorLastName) ? " " + authorLastName : "");
    }

    private AuthorView parseAuthorView() {
        return new AuthorView(null, authorFirstName, authorLastName, null);
    }

    private GenreView parseGenreView() {
        return new GenreView(null, genreName);
    }
}
