package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepositoryImpl extends AbstractRepository<User>
        implements UserRepository {

    @Autowired
    protected UserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
