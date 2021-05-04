package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.contstant.NativeQuery;
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

    @Autowired
    protected TagRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Tag> getEntityType() {
        return Tag.class;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return findByField("name", name);
    }

    @Override
    public Optional<BestUserTag> findUserMostWidelyUsedTagWithHighestOrderCost(long userId) {
        Query query = entityManager.createNativeQuery(
                NativeQuery.MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY,
                "BestTagMapping");
        query.setParameter("userId", userId);

        return buildHelper.getOptionalQueryResult(query);
    }
}
