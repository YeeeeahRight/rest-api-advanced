package com.epam.esm.web.link;

import com.epam.esm.web.controller.GiftCertificateController;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateLinkAdder extends AbstractLinkAdder<GiftCertificateDto> {
    private static final Class<GiftCertificateController> CONTROLLER = GiftCertificateController.class;

    private final LinkAdder<TagDto> tagDtoLinkAdder;

    public GiftCertificateLinkAdder(LinkAdder<TagDto> tagDtoLinkAdder) {
        this.tagDtoLinkAdder = tagDtoLinkAdder;
    }

    @Override
    public void addLinks(GiftCertificateDto entity) {
        long id = entity.getId();
        addIdLinks(CONTROLLER, entity, id, SELF_LINK_NAME, UPDATE_LINK_NAME, DELETE_LINK_NAME);
        if (entity.getTags() != null) {
            entity.getTags().forEach(tagDtoLinkAdder::addLinks);
        }
    }
}
