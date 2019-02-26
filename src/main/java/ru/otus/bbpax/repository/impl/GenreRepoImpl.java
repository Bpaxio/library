package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Genre;
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
@Transactional(readOnly = true)
@SuppressWarnings("JpaQlInspection")
public class GenreRepoImpl implements Repo<Genre, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> getAllQuery = em.createQuery("select g from Genre g", Genre.class);
        return getAllQuery.getResultList();
    }

    @Override
    @Transactional
    public void create(Genre entity) {
        em.persist(entity);
    }

    @Override
    @Transactional
    public void update(Genre entity) {
        Query query = em.createQuery("update Genre g set g.name = :name where g.id = :id");
        query.setParameter("id", entity.getId());
        query.setParameter("name", entity.getName());
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        int rowCount = query.executeUpdate();
        log.info("{} rows were deleted", rowCount);
    }

//    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        try {
            Genre result = query.getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            log.debug("there is no genre with such name {}", name);
            return Optional.empty();
        }
    }
}
