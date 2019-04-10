package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

    default void update(String id, String message) {
        Optional<Comment> byId = findById(id);
        if (byId.isPresent()) {
            Comment comment = byId.get();
            comment.setMessage(message);
            save(comment);
        }
    }

    List<Comment> findAllByBookId(String bookId);

    List<Comment> findAllByUsername(@Param("username") String username);
}
