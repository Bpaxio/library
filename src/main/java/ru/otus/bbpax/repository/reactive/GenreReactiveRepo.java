package ru.otus.bbpax.repository.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Genre;

@Repository
public interface GenreReactiveRepo extends ReactiveMongoRepository<Genre, String> {
}
