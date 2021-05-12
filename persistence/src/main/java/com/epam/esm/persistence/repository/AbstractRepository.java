package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.model.entity.AbstractEntity;
import com.epam.esm.persistence.query.CriteriaQueryBuildHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
public abstract class AbstractRepository<T extends AbstractEntity> implements EntityRepository<T> {

    @PersistenceContext
    protected final EntityManager entityManager;

    protected final CriteriaBuilder builder;
    protected final CriteriaQueryBuildHelper buildHelper;
    protected final Class<T> entityType;

    protected AbstractRepository(EntityManager entityManager, Class<T> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
        this.builder = entityManager.getCriteriaBuilder();
        this.buildHelper = new CriteriaQueryBuildHelper(this.builder);
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<T> getAll(Pageable pageable) {
        CriteriaQuery<T> query = builder.createQuery(entityType);
        Root<T> variableRoot = query.from(entityType);
        query.select(variableRoot);

        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<T> findById(long id) {
        return findByField("id", id);
    }

    @Override
    public Optional<T> findByField(String fieldName, Object value) {
        CriteriaQuery<T> entityQuery = builder.createQuery(entityType);
        Root<T> entityRoot = entityQuery.from(entityType);
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
        T entity = entityManager.find(entityType, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
