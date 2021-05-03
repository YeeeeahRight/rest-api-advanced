package com.epam.esm.service.logic.certificate;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;

import java.util.List;

/**
 * Business logic interface for Certificates.
 */
public interface GiftCertificateService {
    /**
     * Creates new Certificate with optional tags.
     *
     * @param giftCertificateDto Certificate to create with optional tags
     * @return created Certificate
     * @throws InvalidEntityException when Certificate or Tag data is invalid
     */
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    /**
     * Gets list of Certificates.
     *
     * @return List of all existing Certificates
     */
    List<GiftCertificateDto> getAll();

    /**
     * Gets all Certificates with Tags and optional sorting/filtering
     *
     * @param tagName     Tag name to filter Certificates
     * @param partInfo    part info of name/desc to filter Certificates
     * @param sortColumns columns to sort of Certificates
     * @param orderTypes  sort order types
     * @return List of sorted/filtered Certificates with Tags
     * @throws NoSuchEntityException when Tag is not found
     * @throws InvalidParametersException when sort parameters are invalid
     */
    List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                            List<String> sortColumns, List<String> orderTypes);

    /**
     * Gets Certificate by id.
     *
     * @param id Certificate id to search
     * @return founded Certificate
     * @throws NoSuchEntityException when Certificate is not found
     */
    GiftCertificateDto getById(long id);

    /**
     * Updates Certificate by id
     * with updating only fields that are passed
     *
     * @param id              Certificate id to search
     * @param giftCertificateDto update information with Certificate fields or Tags
     * @return updated Certificate with Tags
     * @throws NoSuchEntityException when Certificate is not found
     * @throws InvalidEntityException when update info of Certificate fields is invalid
     */
    GiftCertificateDto updateById(long id, GiftCertificateDto giftCertificateDto);

    /**
     * Deletes Certificate by id.
     *
     * @param id Certificate id to search
     * @throws NoSuchEntityException when Certificate is not found
     */
    void deleteById(long id);
}
