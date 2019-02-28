package ru.otus.bbpax.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface CommonRepo<T, ID> extends PagingAndSortingRepository<T, ID> {
    List<T> findAll();
}
