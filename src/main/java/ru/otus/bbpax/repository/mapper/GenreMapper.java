package ru.otus.bbpax.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.Nullable;
import ru.otus.bbpax.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public class GenreMapper implements RowMapper<Genre>, SourceMapper<Genre> {
    @Nullable
    @Override
    public Genre mapRow(ResultSet rs, int i) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    @Override
    public SqlParameterSource mapSource(Genre entity) {
        return new BeanPropertySqlParameterSource(entity);
    }
}
