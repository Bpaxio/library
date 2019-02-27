package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "books")
public class Author {
    @Id
    @SequenceGenerator(name = "author_seq_gen",
            sequenceName = "author_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_gen")
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    List<Book> books;

    public Author(String name, String surname, String country) {
        this.name = name;
        this.surname = surname;
        this.country = country;
    }

    public Author(Long id, String name, String surname, String country) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.country = country;
    }
}
