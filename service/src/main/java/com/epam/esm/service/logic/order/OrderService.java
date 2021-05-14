package com.epam.esm.service.logic.order;

import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Business logic interface for orders
 */
public interface OrderService {

    /**
     * Creates new Order.
     *
     * @param userId        user id who creates Order
     * @param certificateId certificate to order
     * @return created Order
     * @throws NoSuchEntityException when User or Certificate not found
     */
    Order create(long userId, long certificateId);

    /**
     * Gets all Orders by user id.
     *
     * @param userId User id who has orders
     * @param page   page number of Orders
     * @param size   page size
     * @return founded Orders
     * @throws NoSuchEntityException      when User not found
     * @throws InvalidParametersException when page or size params are invalid
     */
    List<Order> getAllByUserId(long userId, int page, int size);

    /**
     * Gets Order by order and user id.
     *
     * @param userId  User id who has order
     * @param orderId Order id to search
     * @return founded Order
     * @throws NoSuchEntityException when User or Order not found
     */
    Order getByUserId(long userId, long orderId);
}
