package com.epam.esm.service.logic.tag;

import com.epam.esm.persistence.model.BestUserTag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.repository.UserRepository;
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
            throw new DuplicateEntityException("tag.already.exist");
        }

        return tagRepository.create(tag);
    }

    @Override
    public List<Tag> getAll(int page, int size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("pagination.invalid");
        }

        return tagRepository.getAll(pageRequest);
    }

    @Override
    @Transactional
    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }

        return optionalTag.get();
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
