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
import com.epam.esm.web.link.LinkAdder;
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
    private final TagService tagService;

    private final DtoConverter<User, UserDto> userDtoConverter;
    private final LinkAdder<UserDto> userDtoLinkAdder;

    @Autowired
    public UserController(UserService userService,
                          TagService tagService,
                          DtoConverter<User, UserDto> userDtoConverter,
                          LinkAdder<UserDto> userDtoLinkAdder) {
        this.userService = userService;
        this.tagService = tagService;
        this.userDtoConverter = userDtoConverter;
        this.userDtoLinkAdder = userDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        User user = userDtoConverter.convertToEntity(userDto);
        user = userService.create(user);

        UserDto resultUserDto = userDtoConverter.convertToDto(user);
        userDtoLinkAdder.addLinks(resultUserDto);
        return resultUserDto;
    }

    @GetMapping("{id}/best_tag")
    @ResponseStatus(HttpStatus.OK)
    public BestUserTag getBestTag(@PathVariable long id) {
        return tagService.getUserMostWidelyUsedTagWithHighestOrderCost(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "50", required = false) int size){
        List<User> users = userService.getAll(page, size);

        return users.stream()
                .map(userDtoConverter::convertToDto)
                .peek(userDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable long id) {
        User user = userService.getById(id);

        UserDto userDto = userDtoConverter.convertToDto(user);
        userDtoLinkAdder.addLinks(userDto);
        return userDto;
    }
}
