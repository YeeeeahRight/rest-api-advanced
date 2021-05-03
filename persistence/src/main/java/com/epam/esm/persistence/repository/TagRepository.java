package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;

import java.util.Optional;

/**
 * Repository interface for Tag
 */
public interface TagRepository extends EntityRepository<Tag> {

    /**
     * Finds Tag by name.
     *
     * @param name Tag name to find
     * @return Optional Tag - Tag if founded or Empty if not
     */
    Optional<Tag> findByName(String name);
}
