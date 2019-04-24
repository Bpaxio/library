package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.service.error.NotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

    default Comment update(String id, String message) {
        Optional<Comment> byId = findById(id);
        if (byId.isPresent()) {
            Comment comment = byId.get();
            comment.setMessage(message);
            return save(comment);
        }
        throw new NotFoundException("Comment", id);
    }

    List<Comment> findAllByBookId(String bookId);

    List<Comment> findAllByUsername(@Param("username") String username);
}
