package com.epam.esm.service.logic.order;

import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.entity.Order;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.repository.OrderRepository;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.persistence.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.persistence.repository.impl.OrderRepositoryImpl;
import com.epam.esm.persistence.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.user.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderServiceImpl.class})
public class OrderServiceImplTest {
    private static final long ID = 1;
    private static final String NAME = "order";
    private static final User USER = new User(ID, NAME);
    private static final GiftCertificate GIFT_CERTIFICATE = new GiftCertificate();
    private static final Order ORDER = new Order(ID, ZonedDateTime.now(),
            BigDecimal.ONE, USER, GIFT_CERTIFICATE);

    static {
        USER.setOrders(Collections.singletonList(ORDER));
    }

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    @MockBean
    private OrderRepositoryImpl orderRepository;
    @MockBean
    private GiftCertificateRepositoryImpl certificateRepository;
    @MockBean
    private UserRepositoryImpl userRepository;

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        when(certificateRepository.findById(ID)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        orderService.create(ID, ID);
        verify(orderRepository).create(any());
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowsNoSuchEntityExceptionWhenUserNotFound() {
        when(certificateRepository.findById(ID)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        orderService.create(ID, ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowsNoSuchEntityExceptionWhenCertificateNotFound() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        orderService.create(ID, ID);
    }

    @Test
    public void testGetAllByUserIdIdShouldGetAll() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        orderService.getAllByUserId(ID, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(orderRepository).getAllByUserId(eq(ID), any());
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowsInvalidParametersExceptionWhenPaginationInvalid() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        orderService.getAllByUserId(ID, -3, -4);
    }

    @Test
    public void testGetByUserIdShouldGet() {
        when(orderRepository.findById(ID)).thenReturn(Optional.of(ORDER));
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        orderService.getByUserId(ID, ID);
        verify(orderRepository).findById(eq(ID));
        verify(userRepository).findById(eq(ID));
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByUserIdShouldThrowsInvalidParametersExceptionWhenUserNotFound() {
        when(orderRepository.findById(ID)).thenReturn(Optional.of(ORDER));
        when(userRepository.findById(ID)).thenReturn(Optional.empty());
        orderService.getByUserId(ID, ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByUserIdShouldThrowsInvalidParametersExceptionWhenOrderNotFound() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());
        orderService.getByUserId(ID, ID);
    }
}
