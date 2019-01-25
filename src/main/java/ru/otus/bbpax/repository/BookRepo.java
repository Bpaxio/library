package ru.otus.bbpax.repository;

import ru.otus.bbpax.entity.Book;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
public interface BookRepo extends JdbcRepository<Book, Long> {
}
