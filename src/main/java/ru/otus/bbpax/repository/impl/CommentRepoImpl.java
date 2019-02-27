package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.CommentRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional(readOnly = true)
@SuppressWarnings("JpaQlInspection")
public class CommentRepoImpl implements CommentRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllByBookId(Long bookId) {
        TypedQuery<Comment> getAllQuery = em.createQuery("select c from Comment c where c.book = :book", Comment.class);
        Book book = new Book();
        book.setId(bookId);
        getAllQuery.setParameter("book", book);
        return getAllQuery.getResultList();
    }

    @Override
    public List<Comment> findAllByUsername(String username) {
        TypedQuery<Comment> getAllQuery = em.createQuery("select c from Comment c where c.username = :username", Comment.class);
        getAllQuery.setParameter("username", username);
        return getAllQuery.getResultList();
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> getAllQuery = em.createQuery("select c from Comment c", Comment.class);
        return getAllQuery.getResultList();
    }

    @Override
    @Transactional
    public void create(Comment entity) {
        em.persist(entity);
    }

    @Override
    @Transactional
    public void update(Comment entity) {
        Query query = em.createQuery("update Comment c set c.created = :created, " +
                "c.message = :message, c.username = :username, " +
                "c.book = :book " +
                "where c.id = :id");
        query.setParameter("id", entity.getId());
        query.setParameter("created", entity.getCreated());
        query.setParameter("message", entity.getMessage());
        query.setParameter("username", entity.getUsername());
        query.setParameter("book", entity.getBook());
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        int rowCount = query.executeUpdate();
        log.info("{} rows were deleted", rowCount);

    }

    @Override
    @Transactional
    public void update(Long id, String message) {
        Query query = em.createQuery("update Comment c set c.message = :message " +
                "where c.id = :id");
        query.setParameter("id", id);
        query.setParameter("message", message);
        query.executeUpdate();
    }
}
