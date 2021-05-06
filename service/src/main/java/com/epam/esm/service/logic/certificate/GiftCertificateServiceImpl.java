package com.epam.esm.service.logic.certificate;

import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.exception.*;
import com.epam.esm.persistence.model.SortParamsContext;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final Validator<SortParamsContext> sortParametersValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository,
                                      TagRepository tagRepository,
                                      Validator<SortParamsContext> sortParametersValidator) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.sortParametersValidator = sortParametersValidator;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        Set<Tag> tagsToPersist = new HashSet<>();
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tagsToPersist.add(tagOptional.get());
                } else {
                    tagsToPersist.add(tag);
                }
            }
        }
        giftCertificate.setTags(tagsToPersist);

        return certificateRepository.create(giftCertificate);
    }

    @Override
    public List<GiftCertificate> getAll(int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("pagination.invalid");
        }

        List<GiftCertificate> giftCertificates = certificateRepository.getAll(pageRequest);
        giftCertificates.forEach(giftCertificate -> giftCertificate.setTags(null));

        return giftCertificates;
    }

    @Override
    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }

        return certificateOptional.get();
    }

    @Override
    public List<GiftCertificate> getAllWithTagsWithFilteringSorting(List<String> tagNames, String partInfo,
                                                                       List<String> sortColumns, List<String> orderTypes,
                                                                       int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("pagination.invalid");
        }
        SortParamsContext sortParameters = null;
        if (sortColumns != null) {
            sortParameters = new SortParamsContext(sortColumns, orderTypes);
            validateSortParams(sortParameters);
        }

        return certificateRepository.getAllWithSortingFiltering(sortParameters, tagNames, partInfo, pageRequest);
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, GiftCertificate giftCertificate) {
        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        GiftCertificate sourceCertificate = giftCertificateOptional.get();
        setUpdatedFields(sourceCertificate, giftCertificate);
        if (giftCertificate.getTags() != null) {
            Set<Tag> tags = giftCertificate.getTags();
            sourceCertificate.setTags(saveTags(tags));
        }

        return certificateRepository.update(sourceCertificate);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        certificateRepository.deleteById(id);
    }

    private void setUpdatedFields(GiftCertificate sourceCertificate,
                                  GiftCertificate certificateDto) {
//        GiftCertificateValidator giftCertificateValidator =
//                (GiftCertificateValidator) this.giftCertificateValidator;
//        String name = certificateDto.getName();
//        if (name != null) {
//            if (!giftCertificateValidator.isNameValid(name)) {
//                throw new InvalidEntityException("certificate.name.invalid");
//            }
//            sourceCertificate.setName(name);
//        }
//        String description = certificateDto.getDescription();
//        if (description != null) {
//            if (!giftCertificateValidator.isDescriptionValid(description)) {
//                throw new InvalidEntityException("certificate.description.invalid");
//            }
//            sourceCertificate.setDescription(description);
//        }
//        BigDecimal price = certificateDto.getPrice();
//        if (price != null) {
//            if (!giftCertificateValidator.isPriceValid(price)) {
//                throw new InvalidEntityException("certificate.price.invalid");
//            }
//            sourceCertificate.setPrice(price);
//        }
//        int duration = certificateDto.getDuration();
//        if (duration != 0) {
//            if (!giftCertificateValidator.isDurationValid(duration)) {
//                throw new InvalidEntityException("certificate.duration.invalid");
//            }
//            sourceCertificate.setDuration(duration);
//        }
    }

    private Set<Tag> saveTags(Set<Tag> tags) {
        Set<Tag> savedTags = new HashSet<>();
        for (Tag tag : tags) {
            Optional<Tag> optionalTag = tagRepository.findByName(tag.getName());
            Tag savedTag = optionalTag.orElseGet(() -> tagRepository.create(tag));
            savedTags.add(savedTag);
        }
        return savedTags;
    }

    private void validateSortParams(SortParamsContext sortParameters) {
        if (!sortParametersValidator.isValid(sortParameters)) {
            throw new InvalidParametersException("sort.params.invalid");
        }
    }

//    private void validateCertificate(GiftCertificate giftCertificateDto) {
//        if (!giftCertificateValidator.isValid(giftCertificateDto)) {
//            throw new InvalidEntityException("certificate.invalid");
//        }
//    }
//
//    private void validateTags(Set<Tag> tags) {
//        tags.forEach(this::validateTag);
//    }
//
//    private void validateTag(Tag tag) {
//        if (!tagEntityValidator.isValid(tag)) {
//            throw new InvalidEntityException("tag.invalid");
//        }
//    }
//

}
