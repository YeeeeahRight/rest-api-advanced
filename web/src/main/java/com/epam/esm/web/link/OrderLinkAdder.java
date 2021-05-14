package com.epam.esm.web.link;

import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class OrderLinkAdder extends AbstractLinkAdder<OrderDto> {
    private static final Class<UserController> CONTROLLER = UserController.class;

    private final LinkAdder<UserDto> userDtoLinkAdder;
    private final LinkAdder<GiftCertificateDto> certificateDtoLinkAdder;

    public OrderLinkAdder(LinkAdder<UserDto> userDtoLinkAdder,
                          LinkAdder<GiftCertificateDto> certificateDtoLinkAdder) {
        this.userDtoLinkAdder = userDtoLinkAdder;
        this.certificateDtoLinkAdder = certificateDtoLinkAdder;
    }

    @Override
    public void addLinks(OrderDto entity) {
        UserDto userDto = entity.getUserDto();
        entity.add(linkTo(CONTROLLER)
                .slash(userDto.getId())
                .slash("orders")
                .slash(entity.getId())
                .withRel(SELF_LINK_NAME));
        userDtoLinkAdder.addLinks(userDto);
        certificateDtoLinkAdder.addLinks(entity.getCertificate());
    }
}
