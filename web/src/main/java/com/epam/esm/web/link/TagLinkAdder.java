package com.epam.esm.web.link;

import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagLinkAdder extends AbstractLinkAdder<TagDto> {
    private static final Class<TagController> CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto entity) {
        long id = entity.getId();
        addIdLinks(CONTROLLER, entity, id, SELF_LINK_NAME, DELETE_LINK_NAME);
    }
}
