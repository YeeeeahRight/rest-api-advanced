package com.epam.esm.service.logic.tag;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.exception.ExceptionMessageKey;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.exception.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        String tagName = tag.getName();
        boolean isTagExist = tagRepository.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException(ExceptionMessageKey.TAG_EXIST);
        }

        return tagRepository.create(tag);
    }

    @Override
    public List<Tag> getAll(int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(ExceptionMessageKey.INVALID_PAGINATION);
        }

        return tagRepository.getAll(pageRequest);
    }

    @Override
    @Transactional
    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.TAG_NOT_FOUND);
        }

        return optionalTag.get();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.TAG_NOT_FOUND);
        }
        tagRepository.deleteById(id);
    }

    @Override
    public BestUserTag getUserMostWidelyUsedTagWithHighestOrderCost(long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.USER_NOT_FOUND);
        }
        Optional<BestUserTag> bestUserTagOptional = tagRepository
                .findUserMostWidelyUsedTagWithHighestOrderCost(userId);
        if (!bestUserTagOptional.isPresent()) {
            throw new NoSuchEntityException(ExceptionMessageKey.USER_NO_TAGS);
        }
        return bestUserTagOptional.get();
    }
}
