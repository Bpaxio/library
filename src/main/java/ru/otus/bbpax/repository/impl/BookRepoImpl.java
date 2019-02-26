package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.repository.Repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Slf4j
@NoRepositoryBean
//@Repository
@Transactional(readOnly = true)
@SuppressWarnings("JpaQlInspection")
public class BookRepoImpl implements Repo<Book, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> getAllQuery = em.createQuery("select b from Book b", Book.class);
        return getAllQuery.getResultList();
    }

    @Override
    @Transactional
    public void create(Book entity) {
        em.persist(entity);
    }

    @Override
    @Transactional
    public void update(Book entity) {
        Query query = em.createQuery("update Book b set b.name = :name, " +
                "b.publishingOffice = :publishingOffice, b.price = :price, " +
                "b.publicationDate = :publicationDate, " +
                "b.author = :author, b.genre = :genre " +
                "where b.id = :id");
        query.setParameter("id", entity.getId());
        query.setParameter("name", entity.getName());
        query.setParameter("publishingOffice", entity.getPublishingOffice());
        query.setParameter("price", entity.getPrice());
        query.setParameter("publicationDate", entity.getPublicationDate());
        query.setParameter("author", entity.getAuthor());
        query.setParameter("genre", entity.getGenre());
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        int rowCount = query.executeUpdate();
        log.info("{} rows were deleted", rowCount);
    }
}
