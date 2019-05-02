package ru.otus.bbpax.repository.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.bbpax.entity.Comment;

@Repository
public interface CommentReactiveRepo extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findAllByBookId(String bookId);
}
