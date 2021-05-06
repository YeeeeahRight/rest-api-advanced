package com.epam.esm.service.logic;

import static org.mockito.Mockito.*;

import com.epam.esm.persistence.model.SortParamsContext;
import com.epam.esm.persistence.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.persistence.repository.impl.TagRepositoryImpl;
import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.certificate.GiftCertificateServiceImpl;
import com.epam.esm.service.validator.SortParamsContextValidator;
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

    private static final String PART_INFO = "z";
    private static final List<String> SORTING_COLUMN = Collections.singletonList("name");
    private static final SortParamsContext SORT_PARAMS = new SortParamsContext(SORTING_COLUMN, null);

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    @MockBean
    private GiftCertificateRepositoryImpl certificateDao;
    @MockBean
    private TagRepositoryImpl tagDao;
    @MockBean
    private SortParamsContextValidator sortParamsContextValidator;
    @Autowired
    private GiftCertificateServiceImpl giftCertificateService;


    @Test
    public void testCreateShouldCreateWhenNotExistAndValid() {
        giftCertificateService.create(GIFT_CERTIFICATE);
        verify(certificateDao).create(GIFT_CERTIFICATE);
    }

    @Test
    public void testGetAllShouldGetAll() {
        giftCertificateService.getAll(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(certificateDao).getAll(any());
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
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, null, null,
                null, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), any(), any());
    }

    @Test
    public void getAllWithTagsShouldGetWithFilteringWhenFilteringExist() {
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, PART_INFO, null,
                null, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), eq(PART_INFO), any());
    }

    @Test
    public void getAllWithTagsShouldGetWithSortingWhenSoringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, null,
                Collections.singletonList("name"), null, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), any(), any());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringAndFilteringWhenSoringAndFilteringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTagsWithFilteringSorting(null, "p",
                SORTING_COLUMN, null, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(certificateDao).getAllWithSortingFiltering(eq(SORT_PARAMS), any(), anyString(), any());
    }


    @Test
    public void testUpdateByIdShouldUpdateWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        giftCertificateService.updateById(ID, GIFT_CERTIFICATE);
        verify(certificateDao).update(GIFT_CERTIFICATE);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testUpdateByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        giftCertificateService.updateById(ID, GIFT_CERTIFICATE);
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
