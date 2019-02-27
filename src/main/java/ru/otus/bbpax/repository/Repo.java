package ru.otus.bbpax.repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 21.01.2019.
 */
public interface Repo<E, ID> {
    Optional<E> findById(ID id);

    List<E> getAll();

    void create(E entity);

    void update(E entity);

    void deleteById(ID id);
}
