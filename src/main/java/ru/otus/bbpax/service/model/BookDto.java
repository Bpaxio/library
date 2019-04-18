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
public class BookDto implements EntityDto<Book> {

    private String id;
    private String name;
    private Integer publicationDate;
    private String publishingOffice;
    private BigDecimal price;

    private String genreName;
    private String authorFirstName;
    private String authorLastName;

    public BookDto(String name,
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

    public static BookDto fromEntity(Book book) {
        return Objects.isNull(book)
                ? null
                : new BookDto(
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

    public Book toNewEntity() {
        id = null;
        return toEntity();
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

    private AuthorDto parseAuthorView() {
        return new AuthorDto(null, authorFirstName, authorLastName, null);
    }

    private GenreDto parseGenreView() {
        return new GenreDto(null, genreName);
    }

}
