package com.epam.esm.service.logic;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.Set;

/**
 * Business logic interface for Tags.
 */
public interface TagService {
    /**
     * Creates new Tag.
     *
     * @param tagDto Tag to create
     * @return created Tag
     * @throws InvalidEntityException when Tag data is invalid
     * @throws DuplicateEntityException when Tag is already exist
     */
    TagDto create(TagDto tagDto);

    /**
     * Gets all Tags.
     *
     * @return Set of all Tags
     */
    Set<TagDto> getAll();

    /**
     * Gets Tag by id.
     *
     * @param id Tag id to search
     * @return founded Tag
     * @throws NoSuchEntityException when Tag is not found
     */
    TagDto getById(long id);

    /**
     * Deletes Tag by id.
     *
     * @param id Tag id to search
     * @throws NoSuchEntityException when Tag is not found
     */
    void deleteById(long id);
}
