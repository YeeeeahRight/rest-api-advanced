package com.epam.esm.service.logic.user;

import com.epam.esm.persistence.model.entity.User;
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
     */
    User create(User userDto);

    /**
     * Gets all Users.
     *
     * @param page page number of Users
     * @param size page size
     * @return List of all Tags
     * @throws InvalidParametersException when page or size params are invalid
     */
    List<User> getAll(int page, int size);

    /**
     * Gets User by id.
     *
     * @param id User id to search
     * @return founded User
     * @throws NoSuchEntityException when User is not found
     */
    User getById(long id);
}
