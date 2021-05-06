package com.epam.esm.service.logic.tag;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Business logic interface for Tags.
 */
public interface TagService {
    /**
     * Creates new Tag.
     *
     * @param tag Tag to create
     * @return created Tag
     * @throws DuplicateEntityException when Tag is already exist
     */
    Tag create(Tag tag);

    /**
     * Gets all Tags.
     *
     * @param page page number of Tags
     * @param size page size
     * @return List of all Tags
     * @throws InvalidParametersException when page or size params are invalid
     */
    List<Tag> getAll(int page, int size);

    /**
     * Gets Tag by id.
     *
     * @param id Tag id to search
     * @return founded Tag
     * @throws NoSuchEntityException when Tag is not found
     */
    Tag getById(long id);

    /**
     * Deletes Tag by id.
     *
     * @param id Tag id to search
     * @throws NoSuchEntityException when Tag is not found
     */
    void deleteById(long id);

    /**
     * Gets User most widely used Tag with highest Order cost
     *
     * @param userId User id to search
     * @return MostWidelyUsedTag founded Tag
     * @throws NoSuchEntityException when User is not found or user has no tags
     */
    BestUserTag getUserMostWidelyUsedTagWithHighestOrderCost(long userId);
}
