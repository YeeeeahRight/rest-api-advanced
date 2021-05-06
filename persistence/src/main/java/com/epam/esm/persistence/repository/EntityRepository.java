package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.model.entity.AbstractEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Entity repository interface
 *
 * @param <T> the type of entities
 */
public interface EntityRepository<T extends AbstractEntity> {

    /**
     * Creates new entity
     *
     * @param t the entity to be added
     * @return the added entity
     */
    T create(T entity);

    /**
     * Gets all entities.
     *
     * @param pageable object with pagination info(page number, page size)
     * @return List of founded entities
     */
    List<T> getAll(Pageable pageable);

    /**
     * Finds entity by id.
     *
     * @param id entity id to find
     * @return Optional result - Entity if founded or Empty if not
     */
    Optional<T> findById(long id);

    /**
     * Finds entity by column name.
     *
     * @param columnName column name to find
     * @param value column value to find
     * @return Optional result - Entity if founded or Empty if not
     */
    Optional<T> findByField(String columnName, Object value);

    /**
     * Updates entity
     *
     * @param t the entity to be updated
     * @return the updated entity
     */
    T update(T entity);

    /**
     * Removes entity by id.
     *
     * @param id the id of entity to be removed
     */
     void deleteById(long id);
}
