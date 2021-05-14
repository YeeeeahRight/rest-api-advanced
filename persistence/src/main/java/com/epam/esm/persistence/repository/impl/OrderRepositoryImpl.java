package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order> implements OrderRepository {

    @Autowired
    public OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Order.class);
    }

    @Override
    public List<Order> getAllByUserId(long userId, Pageable pageable) {
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);

        Join<User, Order> userJoin = root.join("user");
        Predicate joinIdPredicate = builder.equal(userJoin.get("id"), userId);
        query.where(joinIdPredicate);

        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
