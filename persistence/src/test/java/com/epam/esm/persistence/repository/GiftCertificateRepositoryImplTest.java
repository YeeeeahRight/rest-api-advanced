package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.config.TestJpaConfig;
import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.model.SortParamsContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestJpaConfig.class)
@Transactional
public class GiftCertificateRepositoryImplTest {
    private static final GiftCertificate CERTIFICATE_TO_CREATE = new GiftCertificate(
            "certificate new", "description new", new BigDecimal("1.10"),
            LocalDateTime.parse("2020-01-01T01:11:11").atZone(ZoneId.of("Europe/Moscow")),
            LocalDateTime.parse("2021-01-01T01:22:11").atZone(ZoneId.of("Europe/Moscow")), 1);
    private static final GiftCertificate FIRST_CERTIFICATE = new GiftCertificate(
            1L, "certificate 1", "description 1", new BigDecimal("1.10"),
            LocalDateTime.parse("2020-01-01T01:11:11").atZone(ZoneId.of("Europe/Moscow")),
            LocalDateTime.parse("2021-01-01T01:22:11").atZone(ZoneId.of("Europe/Moscow")), 1);
    private static final GiftCertificate SECOND_CERTIFICATE = new GiftCertificate(
            2L, "certificate 2", "description 2", new BigDecimal("2.20"),
            LocalDateTime.parse("2020-02-02T02:22:22").atZone(ZoneId.of("Europe/Moscow")),
            LocalDateTime.parse("2021-02-02T02:33:22").atZone(ZoneId.of("Europe/Moscow")), 2);
    private static final GiftCertificate THIRD_CERTIFICATE = new GiftCertificate(
            3L, "certificate 3", "description 3", new BigDecimal("3.30"),
            LocalDateTime.parse("2020-03-03T03:33:33").atZone(ZoneId.of("Europe/Moscow")),
            LocalDateTime.parse("2021-03-03T03:44:33").atZone(ZoneId.of("Europe/Moscow")), 3);
    private static final Tag FIRST_TAG = new Tag(1L, "tag 1");
    private static final Tag SECOND_TAG = new Tag(2L, "tag 2");
    private static final Tag THIRD_TAG = new Tag(3L, "tag 3");

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 25);

    static {
        FIRST_CERTIFICATE.setTags(new HashSet<>(Arrays.asList(FIRST_TAG, THIRD_TAG)));
        SECOND_CERTIFICATE.setTags(new HashSet<>(Collections.singletonList(SECOND_TAG)));
        THIRD_CERTIFICATE.setTags(new HashSet<>(Collections.singletonList(THIRD_TAG)));
    }

    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Test
    public void testCreateCertificateShouldCreate() {
        //given
        //when
        GiftCertificate createdCertificate = certificateRepository.create(CERTIFICATE_TO_CREATE);
        //then
        Assert.assertNotNull(createdCertificate);
    }

    @Test
    public void testGetAllShouldGet() {
        //given
        //when
        List<GiftCertificate> giftCertificates = certificateRepository.getAll(DEFAULT_PAGEABLE);
        //then
        Assert.assertEquals(Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE),
                giftCertificates);
    }

    @Test
    public void testGetAllWithSortingFilteringShouldGetSortedCertificates() {
        //given
        SortParamsContext sortParamsContext = new SortParamsContext(
                Collections.singletonList("id"), Collections.singletonList("DESC"));
        //when
        List<GiftCertificate> giftCertificates = certificateRepository.getAllWithSortingFiltering(
                sortParamsContext, null, null, DEFAULT_PAGEABLE);
        //then
        Assert.assertEquals(Arrays.asList(THIRD_CERTIFICATE, SECOND_CERTIFICATE, FIRST_CERTIFICATE),
                giftCertificates);
    }

    @Test
    public void testGetAllWithFilteringShouldGetFilteredCertificates() {
        //given
        //when
        List<GiftCertificate> giftCertificates = certificateRepository.getAllWithSortingFiltering(null,
                Collections.singletonList(THIRD_TAG.getName()), "certif", DEFAULT_PAGEABLE);
        //then
        Assert.assertEquals(Arrays.asList(FIRST_CERTIFICATE, THIRD_CERTIFICATE), giftCertificates);
    }

    @Test
    public void testGetAllWithSortingFilteringShouldGet() {
        //given
        SortParamsContext sortParamsContext = new SortParamsContext(
                Collections.singletonList("id"), Collections.singletonList("DESC"));
        //when
        List<GiftCertificate> giftCertificates = certificateRepository.getAllWithSortingFiltering(
                sortParamsContext, Collections.singletonList(THIRD_TAG.getName()), "certif",
                DEFAULT_PAGEABLE);
        //then
        Assert.assertEquals(Arrays.asList(THIRD_CERTIFICATE, FIRST_CERTIFICATE), giftCertificates);
    }

    @Test
    public void testFindByIdShouldFind() {
        //given
        //when
        Optional<GiftCertificate> giftCertificate = certificateRepository.findById(
                FIRST_CERTIFICATE.getId());
        //then
        Assert.assertEquals(FIRST_CERTIFICATE, giftCertificate.get());
    }

    @Test
    public void testUpdateByIdShouldUpdate() {
        //given
        String savedName = FIRST_CERTIFICATE.getName();
        FIRST_CERTIFICATE.setName("new name");
        //when
        GiftCertificate updatedCertificate = certificateRepository.update(FIRST_CERTIFICATE);
        //then
        Assert.assertEquals(updatedCertificate.getName(), "new name");

        FIRST_CERTIFICATE.setName(savedName);
    }

    @Test
    public void testDeleteByIdShouldDelete() {
        //given
        //when
        certificateRepository.deleteById(THIRD_CERTIFICATE.getId());
        //then
        boolean isExist = certificateRepository.findById(THIRD_CERTIFICATE.getId()).isPresent();
        Assert.assertFalse(isExist);
    }
}
