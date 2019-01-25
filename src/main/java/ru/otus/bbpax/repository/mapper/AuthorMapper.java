package ru.otus.bbpax.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ru.otus.bbpax.entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public class AuthorMapper implements RowMapper<Author> {
    @Nullable
    @Override
    public Author mapRow(ResultSet rs, int i) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String country = rs.getString("country");
        return new Author(id, name, surname, country);
    }
}
