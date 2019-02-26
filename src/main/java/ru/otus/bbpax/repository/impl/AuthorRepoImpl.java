package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.Repo;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
//@Repository
//@Transactional(readOnly = true)
@SuppressWarnings("JpaQlInspection")
public class AuthorRepoImpl implements Repo<Author, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> getAllQuery = em.createQuery("select a from Author a", Author.class);
        return getAllQuery.getResultList();
    }

    @Override
    @Transactional
    public void create(Author entity) {
        em.persist(entity);
    }

    @Override
    @Transactional
    public void update(Author entity) {
        Query query = em.createQuery("update Author a set a.name = :name, a.surname = :surname, a.country = :country where a.id = :id");
        query.setParameter("id", entity.getId());
        query.setParameter("name", entity.getName());
        query.setParameter("surname", entity.getSurname());
        query.setParameter("country", entity.getCountry());
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        int rowCount = query.executeUpdate();
        log.info("{} rows were deleted", rowCount);
    }

//    @Override
    public Optional<Author> findByFullName(String name, String surname) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name and a.surname = :surname", Author.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        try {
            Author result = query.getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            log.debug("there is no author with such name & surname [ {}; {} ]", name, surname);
            return Optional.empty();
        }
    }
}
