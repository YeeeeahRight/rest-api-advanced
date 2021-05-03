package com.epam.esm.service.logic.certificate;

import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.GiftCertificateDtoConverter;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.*;
import com.epam.esm.persistence.query.SortParamsContext;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final Validator<GiftCertificateDto> giftCertificateValidator;
    private final Validator<TagDto> tagEntityValidator;
    private final Validator<SortParamsContext> sortParametersValidator;
    private final GiftCertificateDtoConverter giftCertificateDtoConverter;
    private final TagDtoConverter tagDtoConverter;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository,
                                      TagRepository tagRepository,
                                      Validator<GiftCertificateDto> giftCertificateValidator,
                                      Validator<TagDto> tagEntityValidator,
                                      Validator<SortParamsContext> sortParametersValidator,
                                      GiftCertificateDtoConverter giftCertificateDtoConverter,
                                      TagDtoConverter tagDtoConverter) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagEntityValidator = tagEntityValidator;
        this.sortParametersValidator = sortParametersValidator;
        this.giftCertificateDtoConverter = giftCertificateDtoConverter;
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        validateCertificate(giftCertificateDto);
        validateTags(giftCertificateDto.getTags());
        Set<Tag> tagsToPersist = new HashSet<>();
        for (TagDto tagDto: giftCertificateDto.getTags()) {
            Optional<Tag> tagOptional = tagRepository.findByName(tagDto.getName());
            if (tagOptional.isPresent()) {
                tagsToPersist.add(tagOptional.get());
            } else {
                tagsToPersist.add(tagDtoConverter.convertToEntity(tagDto));
            }
        }
        GiftCertificate giftCertificate = giftCertificateDtoConverter.convertToEntity(giftCertificateDto);
        giftCertificate.setTags(tagsToPersist);
        giftCertificate = certificateRepository.create(giftCertificate);
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> giftCertificates = certificateRepository.getAll();

        return giftCertificates
                .stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .peek(certificateDto -> certificateDto.setTags(null))
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        GiftCertificate giftCertificate = certificateOptional.get();
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                                   List<String> sortColumns, List<String> orderTypes) {
        List<GiftCertificate> giftCertificates;
        boolean isSortExist = sortColumns != null;
        if (isSortExist) {
            SortParamsContext sortParameters = new SortParamsContext(sortColumns, orderTypes);
            validateSortParams(sortParameters);
            if (isFilterExist(tagName, partInfo)) {
                giftCertificates = certificateRepository.getAllWithSortingFiltering(
                        sortParameters, tagName, partInfo);
            } else {
                giftCertificates = certificateRepository.getAllWithSorting(sortParameters);
            }
        } else if (isFilterExist(tagName, partInfo)) {
            giftCertificates = certificateRepository.getAllWithFiltering(tagName, partInfo);
        } else {
            giftCertificates = certificateRepository.getAll();
        }
        return giftCertificates.stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GiftCertificateDto updateById(long id, GiftCertificateDto giftCertificateDto) {
        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        GiftCertificate sourceCertificate = giftCertificateOptional.get();
        setUpdatedFields(sourceCertificate, giftCertificateDto);
        Set<Tag> tags = giftCertificateDto.getTags()
                .stream()
                .map(tagDtoConverter::convertToEntity)
                .collect(Collectors.toSet());
        sourceCertificate.setTags(saveTags(tags));
        GiftCertificate updatedCertificate = certificateRepository.update(sourceCertificate);
        return giftCertificateDtoConverter.convertToDto(updatedCertificate);
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

    private boolean isFilterExist(String tagName, String partInfo) {
        return tagName != null || partInfo != null;
    }

    private void setUpdatedFields(GiftCertificate sourceCertificate,
                                  GiftCertificateDto certificateDto) {
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = certificateDto.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityException("certificate.name.invalid");
            }
            sourceCertificate.setName(name);
        }
        String description = certificateDto.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityException("certificate.description.invalid");
            }
            sourceCertificate.setDescription(description);
        }
        BigDecimal price = certificateDto.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityException("certificate.price.invalid");
            }
            sourceCertificate.setPrice(price);
        }
        int duration = certificateDto.getDuration();
        if (duration != 0) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidEntityException("certificate.duration.invalid");
            }
            sourceCertificate.setDuration(duration);
        }
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

    private void validateCertificate(GiftCertificateDto giftCertificateDto) {
        if (!giftCertificateValidator.isValid(giftCertificateDto)) {
            throw new InvalidEntityException("certificate.invalid");
        }
    }

    private void validateTags(Set<TagDto> tags) {
        tags.forEach(this::validateTag);
    }

    private void validateTag(TagDto tag) {
        if (!tagEntityValidator.isValid(tag)) {
            throw new InvalidEntityException("tag.invalid");
        }
    }

    private void validateSortParams(SortParamsContext sortParameters) {
        if (!sortParametersValidator.isValid(sortParameters)) {
            throw new InvalidParametersException("sort.params.invalid");
        }
    }
}
