package com.epam.esm.service.logic.order;

import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.repository.OrderRepository;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.exception.ExceptionMessageKey;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            GiftCertificateRepository certificateRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order create(long userId, long certificateId) {
        Order order = new Order();
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.USER_NOT_FOUND);
        }
        order.setUser(userOptional.get());
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(certificateId);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.CERTIFICATE_NOT_FOUND);
        }
        GiftCertificate certificate = certificateOptional.get();
        order.setCertificate(certificate);
        order.setCost(certificate.getPrice());

        return orderRepository.create(order);
    }

    @Override
    @Transactional
    public List<Order> getAllByUserId(long userId, int page, int size) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.USER_NOT_FOUND);
        }
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(ExceptionMessageKey.INVALID_PAGINATION);
        }

        return orderRepository.getAllByUserId(userId, pageRequest);
    }

    @Override
    @Transactional
    public Order getByUserId(long userId, long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.ORDER_NOT_FOUND);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.USER_NOT_FOUND);
        }
        Order order = orderOptional.get();
        User user = optionalUser.get();
        List<Order> orders = user.getOrders();
        if (orders == null || orders.isEmpty() || !orders.contains(order)) {
            throw new NoSuchEntityException(ExceptionMessageKey.ORDER_NOT_FOUND);
        }

        return order;
    }
}
