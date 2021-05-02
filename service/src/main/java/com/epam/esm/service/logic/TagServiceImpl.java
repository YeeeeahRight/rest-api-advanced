package com.epam.esm.service.logic;

import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagDao;
    private final Validator<TagDto> tagValidator;
    private final TagDtoConverter tagDtoConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagDao, Validator<TagDto> tagValidator,
                          TagDtoConverter tagDtoConverter) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        if (!tagValidator.isValid(tagDto)) {
            throw new InvalidEntityException("tag.invalid");
        }
        String tagName = tagDto.getName();
        boolean isTagExist = tagDao.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException("tag.already.exist");
        }
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        tag = tagDao.create(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    public Set<TagDto> getAll() {
        return tagDao.getAll()
                        .stream()
                        .map(tagDtoConverter::convertToDto)
                        .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public TagDto getById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }
        return tagDtoConverter.convertToDto(optionalTag.get());
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }
        tagDao.deleteById(id);
    }
}