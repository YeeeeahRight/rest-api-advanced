package com.epam.esm.web.link;

import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserLinkAdder extends AbstractLinkAdder<UserDto> {
    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto entity) {
        long id = entity.getId();
        addIdLink(CONTROLLER, entity, entity.getId(), SELF_LINK_NAME);
        entity.add(linkTo(CONTROLLER)
                .slash(id)
                .slash("orders")
                .withRel("orders"));
    }
}
