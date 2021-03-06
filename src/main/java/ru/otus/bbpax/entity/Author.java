package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.bbpax.repository.listner.annotation.Cascade;
import ru.otus.bbpax.repository.listner.annotation.CascadeType;

import java.util.List;

import static ru.otus.bbpax.entity.EntityTypes.AUTHOR;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "books")
@Document(collection = "authors")
@TypeAlias(AUTHOR)
public class Author implements ListenableEntity {
    @Id
    private String id;

    private String name;

    private String surname;

    private String country;

    @DBRef(lazy = true)
    @Cascade(collection = "books", type = CascadeType.DELETE)
    private List<Book> books;

    public Author(String name, String surname, String country) {
        this.name = name;
        this.surname = surname;
        this.country = country;
    }

    public Author(String id, String name, String surname, String country) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.country = country;
    }
}
