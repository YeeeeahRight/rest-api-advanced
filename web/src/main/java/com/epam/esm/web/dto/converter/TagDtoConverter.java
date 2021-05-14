package com.epam.esm.web.dto.converter;

import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.web.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagDtoConverter implements DtoConverter<Tag, TagDto> {

    @Override
    public Tag convertToEntity(TagDto dto) {
        Tag tag = new Tag();

        tag.setId(dto.getId());
        tag.setName(dto.getName());

        return tag;
    }

    @Override
    public TagDto convertToDto(Tag entity) {
        TagDto tagDto = new TagDto();

        tagDto.setId(entity.getId());
        tagDto.setName(entity.getName());

        return tagDto;
    }
}
