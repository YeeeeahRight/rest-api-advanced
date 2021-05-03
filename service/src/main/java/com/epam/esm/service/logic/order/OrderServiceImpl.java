package com.epam.esm.service.logic.order;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.repository.OrderRepository;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.OrderDtoConverter;
import com.epam.esm.service.exception.NoSuchEntityException;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final OrderDtoConverter orderDtoConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            GiftCertificateRepository certificateRepository,
                            UserRepository userRepository, OrderDtoConverter orderDtoConverter) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.orderDtoConverter = orderDtoConverter;
    }

    @Override
    public OrderDto create(long userId, long certificateId) {
        Order order = new Order();
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("user.not.found");
        }
        order.setUser(userOptional.get());
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(certificateId);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        GiftCertificate certificate = certificateOptional.get();
        order.setCertificate(certificate);
        order.setCost(certificate.getPrice());

        order = orderRepository.create(order);

        return orderDtoConverter.convertToDto(order);
    }

    @Override
    public List<OrderDto> getAllByUserId(long userId) {
        List<Order> orders = orderRepository.getAllByUserId(userId);

        return orders.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getByUserId(long userId, long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new NoSuchEntityException("order.not.found");
        }
        Order order = orderOptional.get();
        if (order.getUser().getId() != userId) {
            throw new NoSuchEntityException("order.not.found");
        }

        return orderDtoConverter.convertToDto(order);
    }
}
