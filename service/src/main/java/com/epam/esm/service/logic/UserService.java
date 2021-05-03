package com.epam.esm.service.logic;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Business logic interface for Users.
 */
public interface UserService {

    /**
     * Creates new User.
     *
     * @param userDto User to create
     * @return created User
     * @throws InvalidEntityException when User data is invalid
     * @throws DuplicateEntityException when User is already exist
     */
    UserDto create(UserDto userDto);

    /**
     * Gets all Users.
     *
     * @return List of all Tags
     */
    List<UserDto> getAll();

    /**
     * Gets User by id.
     *
     * @param id User id to search
     * @return founded User
     * @throws NoSuchEntityException when User is not found
     */
    UserDto getById(long id);
}
