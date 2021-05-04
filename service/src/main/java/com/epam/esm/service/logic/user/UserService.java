package com.epam.esm.service.logic.user;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
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
     */
    UserDto create(UserDto userDto);

    /**
     * Gets all Users.
     *
     * @param page page number of Users
     * @param size page size
     * @return List of all Tags
     * @throws InvalidParametersException when page or size params are invalid
     */
    List<UserDto> getAll(int page, int size);

    /**
     * Gets User by id.
     *
     * @param id User id to search
     * @return founded User
     * @throws NoSuchEntityException when User is not found
     */
    UserDto getById(long id);
}
