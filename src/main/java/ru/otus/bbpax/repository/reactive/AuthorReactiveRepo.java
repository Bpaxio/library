package ru.otus.bbpax.repository.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;

@Repository
public interface AuthorReactiveRepo extends ReactiveMongoRepository<Author, String> {
}
