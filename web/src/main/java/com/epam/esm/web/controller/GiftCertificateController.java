package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.logic.certificate.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.create(giftCertificateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAll() {
        return giftCertificateService.getAll();
    }

    @GetMapping("/with_tags")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags(
            @RequestParam(name = "tag_name", required = false) List<String> tagNames,
            @RequestParam(name = "part_info", required = false) String partInfo,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderTypes) {
        return giftCertificateService.getAllWithTagsWithFilteringSorting(tagNames, partInfo, sortColumns, orderTypes);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto getById(@PathVariable("id") long id) {
        return giftCertificateService.getById(id);
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("id") long id,
                                         @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        giftCertificateService.deleteById(id);
    }
}
