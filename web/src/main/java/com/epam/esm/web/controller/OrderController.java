package com.epam.esm.web.controller;

import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.service.logic.order.OrderService;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.converter.DtoConverter;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    private final DtoConverter<Order, OrderDto> orderDtoConverter;
    private final LinkAdder<OrderDto> orderDtoLinkAdder;

    public OrderController(OrderService orderService, DtoConverter<Order, OrderDto> orderDtoConverter,
                           LinkAdder<OrderDto> orderDtoLinkAdder) {
        this.orderService = orderService;
        this.orderDtoConverter = orderDtoConverter;
        this.orderDtoLinkAdder = orderDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable long userId,
                                @RequestParam(name = "certificate_id") long certificateId) {
        Order order = orderService.create(userId, certificateId);

        OrderDto orderDto = orderDtoConverter.convertToDto(order);
        orderDtoLinkAdder.addLinks(orderDto);
        return orderDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(@PathVariable long userId,
                                       @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                       @RequestParam(value = "size", defaultValue = "4", required = false) int size) {
        List<Order> orders = orderService.getAllByUserId(userId, page, size);

        return orders.stream()
                .map(orderDtoConverter::convertToDto)
                .peek(orderDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable long userId, @PathVariable long orderId) {
        Order order = orderService.getByUserId(userId, orderId);

        OrderDto orderDto = orderDtoConverter.convertToDto(order);
        orderDtoLinkAdder.addLinks(orderDto);
        return orderDto;
    }
}
