package ru.otus.bbpax.repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public interface JdbcRepository<E, ID> {
    Optional<E> findById(ID id);

    List<E> getAll();

    void create(E entity);

    void update(E entity);

    // TODO: 2019-01-29 Запрос не вернул результатов.; nested exception is org.postgresql.util.PSQLException: Запрос не вернул результатов.
    //Details of the error have been omitted
    void deleteById(ID id);
}
