package com.epam.esm.service.logic;

import static org.mockito.Mockito.*;

import com.epam.esm.persistence.model.SortParamsContext;
import com.epam.esm.persistence.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.persistence.repository.impl.TagRepositoryImpl;
import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.GiftCertificateDtoConverter;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.certificate.GiftCertificateServiceImpl;
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
import java.util.List;
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

    private static final String PART_INFO = "z";
    private static final List<String> SORTING_COLUMN = Collections.singletonList("name");
    private static final SortParamsContext SORT_PARAMS = new SortParamsContext(SORTING_COLUMN, null);

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
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, null, null, null);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), any());
    }

    @Test
    public void getAllWithTagsShouldGetWithFilteringWhenFilteringExist() {
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, PART_INFO, null, null);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), eq(PART_INFO));
    }

    @Test
    public void getAllWithTagsShouldGetWithSortingWhenSoringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, null,
                Collections.singletonList("name"), null);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), any());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringAndFilteringWhenSoringAndFilteringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, "p",
                SORTING_COLUMN, null);
        verify(certificateDao).getAllWithSortingFiltering(eq(SORT_PARAMS), any(), anyString());
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
