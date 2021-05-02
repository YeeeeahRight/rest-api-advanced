package com.epam.esm.service.dto.converter;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoConverter
        implements DtoConverter<GiftCertificate, GiftCertificateDto> {
    private final TagDtoConverter tagDtoConverter;

    @Autowired
    public GiftCertificateDtoConverter(TagDtoConverter tagDtoConverter) {
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    public GiftCertificate convertToEntity(GiftCertificateDto dto) {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(dto.getId());
        giftCertificate.setName(dto.getName());
        giftCertificate.setDescription(dto.getDescription());
        giftCertificate.setDuration(dto.getDuration());
        giftCertificate.setPrice(dto.getPrice());
        giftCertificate.setCreateDate(dto.getCreateDate());
        giftCertificate.setLastUpdateDate(dto.getLastUpdateDate());
        Set<Tag> tags = dto.getTags()
                .stream()
                .map(tagDtoConverter::convertToEntity).collect(Collectors.toSet());
        giftCertificate.setTags(tags);

        return giftCertificate;
    }

    @Override
    public GiftCertificateDto convertToDto(GiftCertificate entity) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();

        giftCertificateDto.setId(entity.getId());
        giftCertificateDto.setName(entity.getName());
        giftCertificateDto.setDescription(entity.getDescription());
        giftCertificateDto.setDuration(entity.getDuration());
        giftCertificateDto.setPrice(entity.getPrice());
        giftCertificateDto.setCreateDate(entity.getCreateDate());
        giftCertificateDto.setLastUpdateDate(entity.getLastUpdateDate());
        if (entity.getTags() != null) {
            Set<TagDto> dtoTags = entity.getTags()
                    .stream()
                    .map(tagDtoConverter::convertToDto).collect(Collectors.toSet());
            giftCertificateDto.setTags(dtoTags);
        }

        return giftCertificateDto;
    }
}
