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

import static ru.otus.bbpax.entity.EntityTypes.GENRE;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "books")
@Document(collection = "genres")
@TypeAlias(GENRE)
public class Genre implements ListenableEntity {
    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    @Cascade(type = CascadeType.DELETE, collection = "books")
    List<Book> books;

    public Genre(String name) {
        this.name = name;
    }

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
