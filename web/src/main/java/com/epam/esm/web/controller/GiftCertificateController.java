package com.epam.esm.web.controller;

import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.service.logic.certificate.GiftCertificateService;
import com.epam.esm.web.dto.converter.DtoConverter;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    private final DtoConverter<GiftCertificate, GiftCertificateDto> certificateDtoConverter;
    private final LinkAdder<GiftCertificateDto> certificateDtoLinkAdder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     DtoConverter<GiftCertificate, GiftCertificateDto> certificateDtoConverter,
                                     LinkAdder<GiftCertificateDto> certificateDtoLinkAdder) {
        this.giftCertificateService = giftCertificateService;
        this.certificateDtoConverter = certificateDtoConverter;
        this.certificateDtoLinkAdder = certificateDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = certificateDtoConverter.convertToEntity(giftCertificateDto);
        giftCertificate = giftCertificateService.create(giftCertificate);

        GiftCertificateDto resultCertificateDto = certificateDtoConverter.convertToDto(giftCertificate);
        certificateDtoLinkAdder.addLinks(resultCertificateDto);

        return resultCertificateDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<GiftCertificate> certificates = giftCertificateService.getAll(page, size);

        return certificates.stream()
                .map(certificateDtoConverter::convertToDto)
                .peek(certificateDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/with_tags")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags(
            @RequestParam(name = "tag_name", required = false) List<String> tagNames,
            @RequestParam(name = "part_info", required = false) String partInfo,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderTypes,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<GiftCertificate> certificates = giftCertificateService.getAllWithTagsWithFilteringSorting(
                tagNames, partInfo, sortColumns, orderTypes, page, size);

        GiftCertificateDto resultCertificateDto = certificateDtoConverter.convertToDto(certificates.get(0));
        certificateDtoLinkAdder.addLinks(resultCertificateDto);

        return Arrays.asList(resultCertificateDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto getById(@PathVariable("id") long id) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);

        GiftCertificateDto certificateDto = certificateDtoConverter.convertToDto(giftCertificate);
        certificateDtoLinkAdder.addLinks(certificateDto);
        return certificateDto;
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("id") long id,
                                         @RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = certificateDtoConverter.convertToEntity(giftCertificateDto);
        giftCertificate = giftCertificateService.updateById(id, giftCertificate);

        GiftCertificateDto resultCertificateDto = certificateDtoConverter.convertToDto(giftCertificate);
        certificateDtoLinkAdder.addLinks(resultCertificateDto);
        return resultCertificateDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        giftCertificateService.deleteById(id);
    }
}
