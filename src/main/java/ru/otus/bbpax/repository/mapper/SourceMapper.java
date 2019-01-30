package ru.otus.bbpax.repository.mapper;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface SourceMapper<T> {
    SqlParameterSource mapSource(T entity);
}
