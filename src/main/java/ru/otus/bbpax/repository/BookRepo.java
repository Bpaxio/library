package ru.otus.bbpax.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface BookRepo extends PagingAndSortingRepository<Book, Long> {
    List<Book> findAll();
}
