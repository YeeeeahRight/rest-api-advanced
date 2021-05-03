package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Order;

import java.util.List;

/**
 * Repository interface for Order
 */
public interface OrderRepository extends EntityRepository<Order> {

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     *
     * @return founded orders
     */
    List<Order> getAllByUserId(long userId);
}
