package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.model.entity.Tag;

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

    /**
     * Gets User most widely used Tag with highest Order cost
     *
     * @param userId User id to search
     * @return Optional BestUserTag - BestUserTag if founded or Empty if not
     */
    Optional<BestUserTag> findUserMostWidelyUsedTagWithHighestOrderCost(long userId);
}
