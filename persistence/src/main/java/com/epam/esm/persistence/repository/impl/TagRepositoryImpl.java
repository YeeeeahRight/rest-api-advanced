package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
}
