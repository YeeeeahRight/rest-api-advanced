package com.epam.esm.web.link;

import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class AbstractLinkAdder<T extends RepresentationModel<T>> implements LinkAdder<T> {
    protected final String SELF_LINK_NAME = "self";
    protected final String UPDATE_LINK_NAME = "update";
    protected final String DELETE_LINK_NAME = "delete";

    protected void addIdLink(Class<?> controllerClass, T entity, long id, String linkName) {
        entity.add(linkTo(controllerClass).slash(id).withRel(linkName));
    }

    protected void addIdLinks(Class<?> controllerClass, T entity, long id, String ...linkNames) {
        for (String linkName : linkNames) {
            addIdLink(controllerClass, entity, id, linkName);
        }
    }
}
