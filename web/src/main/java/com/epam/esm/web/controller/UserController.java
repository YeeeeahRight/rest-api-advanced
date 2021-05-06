package com.epam.esm.web.controller;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.service.logic.order.OrderService;
import com.epam.esm.service.logic.tag.TagService;
import com.epam.esm.service.logic.user.UserService;
import com.epam.esm.web.dto.converter.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final TagService tagService;

    private final DtoConverter<User, UserDto> userDtoConverter;
    private final DtoConverter<Order, OrderDto> orderDtoConverter;


    @Autowired
    public UserController(UserService userService,
                          OrderService orderService,
                          TagService tagService,
                          DtoConverter<User, UserDto> userDtoConverter,
                          DtoConverter<Order, OrderDto> orderDtoConverter) {
        this.userService = userService;
        this.orderService = orderService;
        this.tagService = tagService;
        this.userDtoConverter = userDtoConverter;
        this.orderDtoConverter = orderDtoConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        User user = userDtoConverter.convertToEntity(userDto);
        user = userService.create(user);

        return userDtoConverter.convertToDto(user);
    }

    @PostMapping("{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable long id,
                                @RequestParam(name = "certificate_id") long certificateId) {
        Order order = orderService.create(id, certificateId);

        return orderDtoConverter.convertToDto(order);
    }

    @GetMapping("{id}/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(@PathVariable long id,
                                       @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                       @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<Order> orders = orderService.getAllByUserId(id, page, size);

        return orders.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable long id, @PathVariable long orderId) {
        Order order = orderService.getByUserId(id, orderId);

        return orderDtoConverter.convertToDto(order);
    }

    @GetMapping("{id}/best_tag")
    @ResponseStatus(HttpStatus.OK)
    public BestUserTag getBestTag(@PathVariable long id) {
        return tagService.getUserMostWidelyUsedTagWithHighestOrderCost(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "25", required = false) int size){
        List<User> users = userService.getAll(page, size);

        return users.stream()
                .map(userDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable long id) {
        User user = userService.getById(id);

        return userDtoConverter.convertToDto(user);
    }
}
