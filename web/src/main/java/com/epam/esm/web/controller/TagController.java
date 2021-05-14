package com.epam.esm.web.controller;

import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.service.logic.tag.TagService;
import com.epam.esm.web.dto.converter.DtoConverter;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final LinkAdder<TagDto> tagDtoLinkAdder;

    @Autowired
    public TagController(TagService tagService, DtoConverter<Tag, TagDto> tagDtoConverter,
                         LinkAdder<TagDto> tagDtoLinkAdder) {
        this.tagService = tagService;
        this.tagDtoConverter = tagDtoConverter;
        this.tagDtoLinkAdder = tagDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        tag = tagService.create(tag);

        TagDto resultTagDto = tagDtoConverter.convertToDto(tag);
        tagDtoLinkAdder.addLinks(resultTagDto);
        return resultTagDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                               @RequestParam(value = "size", defaultValue = "50", required = false) int size) {
        List<Tag> tags = tagService.getAll(page, size);

        return tags.stream()
                .map(tagDtoConverter::convertToDto)
                .peek(tagDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto getById(@PathVariable("id") long id) {
        Tag tag = tagService.getById(id);

        TagDto resultTagDto = tagDtoConverter.convertToDto(tag);
        tagDtoLinkAdder.addLinks(resultTagDto);
        return resultTagDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        tagService.deleteById(id);
    }
}
