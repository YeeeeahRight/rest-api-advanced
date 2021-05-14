package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.model.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository interface for Order
 */
public interface OrderRepository extends EntityRepository<Order> {

    /**
     * Gets all Orders by User id
     *
     * @param userId User id to search
     * @param pageable object with pagination info(page number, page size)
     * @return founded orders
     */
    List<Order> getAllByUserId(long userId, Pageable pageable);
}
