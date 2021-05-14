package com.epam.esm.web.dto.converter;

import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoConverter implements DtoConverter<Order, OrderDto> {

    private final GiftCertificateDtoConverter certificateDtoConverter;
    private final UserDtoConverter userDtoConverter;

    @Autowired
    public OrderDtoConverter(GiftCertificateDtoConverter certificateDtoConverter,
                             UserDtoConverter userDtoConverter) {
        this.certificateDtoConverter = certificateDtoConverter;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public Order convertToEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setCost(dto.getCost());
        order.setOrderDate(dto.getCreateDate());
        UserDto userDto = dto.getUserDto();
        order.setUser(userDto == null ? null : userDtoConverter.convertToEntity(userDto));
        GiftCertificateDto giftCertificateDto = dto.getCertificate();
        order.setCertificate(giftCertificateDto == null ? null :
                certificateDtoConverter.convertToEntity(giftCertificateDto));

        return order;
    }

    @Override
    public OrderDto convertToDto(Order entity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(entity.getId());
        orderDto.setCreateDate(entity.getOrderDate());
        orderDto.setCost(entity.getCost());
        GiftCertificate giftCertificate = entity.getCertificate();
        orderDto.setCertificate(certificateDtoConverter.convertToDto(giftCertificate));
        User user = entity.getUser();
        orderDto.setUserDto(userDtoConverter.convertToDto(user));

        return orderDto;
    }
}
