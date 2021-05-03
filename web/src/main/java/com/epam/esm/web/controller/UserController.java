package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.logic.order.OrderService;
import com.epam.esm.service.logic.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService,
                          OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@PathVariable long id,
                                @RequestParam(name = "certificate_id") long certificateId) {
        return orderService.create(id, certificateId);
    }

    @GetMapping("{id}/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(@PathVariable long id) {
        return orderService.getAllByUserId(id);
    }

    @GetMapping("{id}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable long id, @PathVariable long orderId) {
        return orderService.getByUserId(id, orderId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getAll(@PathVariable long id) {
        return userService.getById(id);
    }
}
