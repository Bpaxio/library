package ru.otus.bbpax.repository;

import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Genre;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Repository
public class GenreRepoImpl implements GenreRepo {
    @Override
    public Optional<Genre> getById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<Genre> getAll() {
        throw new NotImplementedException();
    }

    @Override
    public Long update(Genre entity) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Genre entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Long id) {
        throw new NotImplementedException();
    }
}
