package ru.otus.bbpax.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.configuration.serialization.CustomBigDecimalDeserializer;
import ru.otus.bbpax.configuration.serialization.CustomBigDecimalSerializer;
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
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal price;

    private String genreId;
    private String authorId;

    public BookDto(String name,
                   Integer publicationDate,
                   String publishingOffice,
                   BigDecimal price,
                   String genreId,
                   String authorId
    ) {
        this.name = name;
        this.publicationDate = publicationDate;
        this.publishingOffice = publishingOffice;
        this.price = price;
        this.genreId = genreId;
        this.authorId = authorId;
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
                        book.getGenre().getId(),
                        book.getAuthor().getId()
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

    private AuthorDto parseAuthorView() {
        return new AuthorDto(authorId, null, null, null);
    }

    private GenreDto parseGenreView() {
        return new GenreDto(genreId, null);
    }

}
