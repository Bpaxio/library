package ru.otus.bbpax.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.Nullable;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public class BookMapper implements RowMapper<Book>, SourceMapper<Book> {

    @Nullable
    @Override
    public Book mapRow(ResultSet rs, int i) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Integer publicationDate = rs.getInt("publication_date");
        String publishingOffice = rs.getString("publishing_office");
        BigDecimal price = new BigDecimal(rs.getString("price"));

        Long genreId = rs.getLong("genre_id");
        String genreName = rs.getString("genre_name");
        Genre genre = new Genre(genreId, genreName);

        Long authorId = rs.getLong("author_id");
        String authorName = rs.getString("author_name");
        String authorSurname = rs.getString("author_surname");
        String authorCountry = rs.getString("author_country");
        Author author = new Author(authorId, authorName, authorSurname, authorCountry);
        return new Book(id, name, publicationDate, publishingOffice, price, genre, author);
    }

    @Override
    public SqlParameterSource mapSource(Book entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("name", entity.getName());
        map.put("publication_date", entity.getPublicationDate());
        map.put("price", entity.getPrice());
        map.put("publishing_office", entity.getPublishingOffice());
        map.put("genre_id", entity.getGenre().getId());
        map.put("author_id", entity.getAuthor().getId());
        return new MapSqlParameterSource(map);
//        return new BeanPropertySqlParameterSource(entity);
    }
}
