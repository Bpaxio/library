package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @SequenceGenerator(name = "book_seq_gen",
            sequenceName = "book_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq_gen")
    private Long id;

    @Column
    private String name;

    @Column
    private Integer publicationDate;

    @Column
    private String publishingOffice;

    @Column
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

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
}
