package ru.otus.bbpax.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.otus.bbpax.repository.impl.AuthorRepoImpl;
import ru.otus.bbpax.repository.impl.BookRepoImpl;
import ru.otus.bbpax.repository.impl.GenreRepoImpl;

import javax.sql.DataSource;


@Configuration
public class RepoConfig {


    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/db/changelog/scheme-h2.sql")
                .build();
    }

    @Bean
    public NamedParameterJdbcOperations jdbcOperations(DataSource database) {
        return new NamedParameterJdbcTemplate(database);
    }

    @Bean
    public AuthorRepo authorRepo(NamedParameterJdbcOperations jdbc) {
        return new AuthorRepoImpl(jdbc);
    }

    @Bean
    public GenreRepo genreRepo(NamedParameterJdbcOperations jdbc) {
        return new GenreRepoImpl(jdbc);
    }

    @Bean
    public BookRepo bookRepo(NamedParameterJdbcOperations jdbc) {
        return new BookRepoImpl(jdbc);
    }
}
