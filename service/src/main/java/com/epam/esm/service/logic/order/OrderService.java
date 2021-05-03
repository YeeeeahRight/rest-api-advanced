package com.epam.esm.service.logic.order;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Business logic interface for orders
 */
public interface OrderService {

    /**
     * Creates new Order.
     *
     * @param userId user id who creates Order
     * @param certificateId certificate to order
     *
     * @return created Order
     * @throws NoSuchEntityException when User or Certificate not found
     */
    OrderDto create(long userId, long certificateId);

    /**
     * Gets all Orders by user id.
     *
     * @param userId User id who has orders
     *
     * @return founded Orders
     * @throws NoSuchEntityException when User not found
     */
    List<OrderDto> getAllByUserId(long userId);

    /**
     * Gets Order by order and user id.
     *
     * @param userId User id who has order
     * @param orderId Order id to search
     *
     * @return founded Order
     * @throws NoSuchEntityException when User or Order not found
     */
    OrderDto getByUserId(long userId, long orderId);
}
