package com.epam.esm.web.link;

import org.springframework.hateoas.RepresentationModel;

/**
 * Representation model link adder for HATEOAS
 *
 * @param <T> type of representation model
 */
public interface LinkAdder<T extends RepresentationModel<T>> {

    /**
     * Adds links to model
     *
     * @param entity that will be filled with links
     */
    void addLinks(T entity);
}
