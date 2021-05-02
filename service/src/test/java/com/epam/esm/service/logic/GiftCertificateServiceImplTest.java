package com.epam.esm.service.logic;

import static org.mockito.Mockito.*;

import com.epam.esm.persistence.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.persistence.repository.impl.TagRepositoryImpl;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.GiftCertificateDtoConverter;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.SortParamsContextValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GiftCertificateServiceImpl.class)
public class GiftCertificateServiceImplTest {
    private static final long ID = 1;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final ZonedDateTime UPDATE_TIME = ZonedDateTime.now();
    private static final ZonedDateTime CREATE_TIME = ZonedDateTime.now();
    private static final int DURATION = 5;
    private static final GiftCertificate GIFT_CERTIFICATE = new GiftCertificate(
            ID, NAME, DESCRIPTION,PRICE, CREATE_TIME, UPDATE_TIME, DURATION
    );
    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO = new GiftCertificateDto(
            ID, NAME, DESCRIPTION,PRICE, CREATE_TIME, UPDATE_TIME, DURATION
    );

    @MockBean
    private GiftCertificateRepositoryImpl certificateDao;
    @MockBean
    private GiftCertificateValidator certificateValidator;
    @MockBean
    private TagRepositoryImpl tagDao;
    @MockBean
    private TagValidator tagValidator;
    @MockBean
    private SortParamsContextValidator sortParamsContextValidator;
    @MockBean
    private GiftCertificateDtoConverter giftCertificateDtoConverter;
    @MockBean
    private TagDtoConverter tagDtoConverter;

    @Autowired
    private GiftCertificateServiceImpl giftCertificateService;

    @Before
    public void init() {
        when(giftCertificateDtoConverter.convertToDto(GIFT_CERTIFICATE)).thenReturn(GIFT_CERTIFICATE_DTO);
        when(giftCertificateDtoConverter.convertToEntity(GIFT_CERTIFICATE_DTO)).thenReturn(GIFT_CERTIFICATE);
    }

    @Test
    public void testCreateShouldCreateWhenNotExistAndValid() {
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(true);
        giftCertificateService.create(GIFT_CERTIFICATE_DTO);
        verify(certificateDao).create(GIFT_CERTIFICATE);
    }

    @Test(expected = InvalidEntityException.class)
    public void testCreateShouldThrowsInvalidEntityExceptionWhenNotValid() {
        when(certificateValidator.isValid(any())).thenReturn(false);
        giftCertificateService.create(GIFT_CERTIFICATE_DTO);
    }

    @Test
    public void testGetAllShouldGetAll() {
        giftCertificateService.getAll();
        verify(certificateDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        giftCertificateService.getById(ID);
        verify(certificateDao).findById(ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowsNotSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        giftCertificateService.getById(ID);
    }

    @Test
    public void getAllWithTagsShouldGetAllWhenFilteringAndSortingNotExist() {
        giftCertificateService.getAllWithTags(null, null, null, null);
        verify(certificateDao).getAll();
    }

    @Test
    public void getAllWithTagsShouldGetWithFilteringWhenFilteringExist() {
        giftCertificateService.getAllWithTags(null, "z", null, null);
        verify(certificateDao).getAllWithFiltering(any(), anyString());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringWhenSoringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTags(null, null,
                Collections.singletonList("name"), null);
        verify(certificateDao).getAllWithSorting(any());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringAndFilteringWhenSoringAndFilteringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTags(null, "p",
                Collections.singletonList("name"), null);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), anyString());
    }


    @Test
    public void testUpdateByIdShouldUpdateWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(certificateValidator.isNameValid(anyString())).thenReturn(true);
        when(certificateValidator.isDescriptionValid(anyString())).thenReturn(true);
        when(certificateValidator.isDurationValid(anyInt())).thenReturn(true);
        when(certificateValidator.isPriceValid(any())).thenReturn(true);
        giftCertificateService.updateById(ID, GIFT_CERTIFICATE_DTO);
        verify(certificateDao).update(GIFT_CERTIFICATE);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testUpdateByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        giftCertificateService.updateById(ID, GIFT_CERTIFICATE_DTO);
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        giftCertificateService.deleteById(ID);
        verify(certificateDao).deleteById(ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        giftCertificateService.deleteById(ID);
    }
}
