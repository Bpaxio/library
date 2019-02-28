package ru.otus.bbpax.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Entity
@Data
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "book")
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

    public Book(Long id,
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
}
