package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.query.NativeQuery;
import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Repository
@Transactional
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {
    private static final String BEST_TAG_MAPPING_NAME = "BestTagMapping";

    @Autowired
    protected TagRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Tag.class);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return findByField("name", name);
    }

    @Override
    public Optional<BestUserTag> findUserMostWidelyUsedTagWithHighestOrderCost(long userId) {
        Query query = entityManager.createNativeQuery(
                NativeQuery.MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY, BEST_TAG_MAPPING_NAME);
        query.setParameter("userId", userId);

        return buildHelper.getOptionalQueryResult(query);
    }
}
