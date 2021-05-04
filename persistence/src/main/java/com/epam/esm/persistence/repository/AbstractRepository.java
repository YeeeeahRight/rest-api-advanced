package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.query.CriteriaQueryBuildHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;


/**
 * CRUD abstract repository class
 *
 * @param <T> the type of entities
 */
@Transactional
public abstract class AbstractRepository<T> implements EntityRepository<T> {

    @PersistenceContext
    protected final EntityManager entityManager;
    protected final CriteriaBuilder builder;
    protected final CriteriaQueryBuildHelper buildHelper;

    protected AbstractRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.builder = entityManager.getCriteriaBuilder();
        this.buildHelper = new CriteriaQueryBuildHelper(this.builder);
    }

    protected abstract Class<T> getEntityType();

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<T> getAll() {
        CriteriaQuery<T> query = builder.createQuery(getEntityType());
        Root<T> variableRoot = query.from(getEntityType());
        query.select(variableRoot);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<T> findById(long id) {
        return findByField("id", id);
    }

    @Override
    public Optional<T> findByField(String fieldName, Object value) {
        CriteriaQuery<T> entityQuery = builder.createQuery(getEntityType());
        Root<T> entityRoot = entityQuery.from(getEntityType());
        entityQuery.select(entityRoot);

        Predicate fieldPredicate = builder.equal(entityRoot.get(fieldName), value);
        entityQuery.where(fieldPredicate);

        TypedQuery<T> typedQuery = entityManager.createQuery(entityQuery);
        return buildHelper.getOptionalQueryResult(typedQuery);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(long id) {
        T entity = entityManager.find(getEntityType(), id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
