package ru.otus.bbpax.repository;

import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Repository
public class BookRepoImpl implements BookRepo {
    @Override
    public Optional<Book> getById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<Book> getAll() {
        throw new NotImplementedException();
    }

    @Override
    public Long update(Book entity) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Book entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Long id) {
        throw new NotImplementedException();
    }
}
