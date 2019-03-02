package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

    @Query(value = "{'id': ?1}, {$set: {'message': ?2}}")
    void update(@Param("id") String id, @Param("message") String message);

    List<Comment> findAllByBookId(String bookId);

    List<Comment> findAllByUsername(@Param("username") String username);
}
