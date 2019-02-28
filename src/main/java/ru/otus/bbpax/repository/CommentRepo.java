package ru.otus.bbpax.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends CommonRepo<Comment, Long> {

    @Modifying
    @Query(value = "update Comment c set c.message = :message where c.id = :id")
    void update(@Param("id") Long id, @Param("message") String message);

    List<Comment> findAllByBookId(Long bookId);

    List<Comment> findAllByUsername(@Param("username") String username);
}
