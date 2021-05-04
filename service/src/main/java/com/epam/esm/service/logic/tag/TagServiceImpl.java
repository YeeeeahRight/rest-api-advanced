package com.epam.esm.service.logic.tag;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final Validator<TagDto> tagValidator;
    private final TagDtoConverter tagDtoConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          UserRepository userRepository,
                          Validator<TagDto> tagValidator,
                          TagDtoConverter tagDtoConverter) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.tagValidator = tagValidator;
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        if (!tagValidator.isValid(tagDto)) {
            throw new InvalidEntityException("tag.invalid");
        }
        String tagName = tagDto.getName();
        boolean isTagExist = tagRepository.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException("tag.already.exist");
        }
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        tag = tagRepository.create(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    public Set<TagDto> getAll(int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("pagination.invalid");
        }
        List<Tag> tags = tagRepository.getAll(pageRequest);

        return  tags.stream()
                .map(tagDtoConverter::convertToDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public TagDto getById(long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }
        return tagDtoConverter.convertToDto(optionalTag.get());
    }

    @Override
    public BestUserTag getUserMostWidelyUsedTagWithHighestOrderCost(long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NoSuchEntityException("user.not.found");
        }
        Optional<BestUserTag> bestUserTagOptional = tagRepository
                .findUserMostWidelyUsedTagWithHighestOrderCost(userId);
        if (!bestUserTagOptional.isPresent()) {
            throw new NoSuchEntityException("user.no.tags");
        }
        return bestUserTagOptional.get();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }
        tagRepository.deleteById(id);
    }
}
