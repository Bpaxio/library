package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.otus.bbpax.entity.EntityTypes.BOOK;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
@TypeAlias(BOOK)
public class Book implements ListenableEntity {

    @Id
    @Field("_id")
    private String id;
    private String name;
    private Integer publicationDate;
    private String publishingOffice;
    private BigDecimal price;

    @DBRef
    private Genre genre;

    @DBRef
    private Author author;

    @DBRef(lazy = true)
    private List<Comment> comments;

    public Book(String name,
                Integer publicationDate,
                String publishingOffice,
                BigDecimal price,
                Genre genre,
                Author author) {
        this.name = name;
        this.publicationDate = publicationDate;
        this.publishingOffice = publishingOffice;
        this.price = price;
        this.genre = genre;
        this.author = author;
    }

    public Book(String id,
                String name,
                Integer publicationDate,
                String publishingOffice,
                BigDecimal price,
                Genre genre,
                Author author) {
        this.id = id;
        this.name = name;
        this.publicationDate = publicationDate;
        this.publishingOffice = publishingOffice;
        this.price = price;
        this.genre = genre;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", publicationDate=" + publicationDate
                + ", publishingOffice='" + publishingOffice + '\''
                + ", price=" + price
                + ", genre=" + genre
                + ", author=" + author
                + ", comments= [" + comments.stream()
                                        .filter(Objects::nonNull)
                                        .map(Comment::getId)
                                        .collect(Collectors.toList())
                + "]}";
    }
}
