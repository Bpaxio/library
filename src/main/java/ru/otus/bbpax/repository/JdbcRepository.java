package ru.otus.bbpax.repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public interface JdbcRepository<E, ID> {
    Optional<E> getById(ID id);

    List<E> getAll();

    ID update(E entity);

    void delete(E entity);
    void deleteById(ID id);
}
