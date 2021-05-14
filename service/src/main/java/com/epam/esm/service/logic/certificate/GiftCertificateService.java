package com.epam.esm.service.logic.certificate;

import com.epam.esm.persistence.model.entity.GiftCertificate;
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
     * @param giftCertificate Certificate to create with optional tags
     * @return created Certificate
     */
    GiftCertificate create(GiftCertificate giftCertificate);

    /**
     * Gets list of Certificates.
     *
     * @param page page number of Certificates
     * @param size page size
     * @return List of all existing Certificates
     * @throws InvalidParametersException when page or size params are invalid
     */
    List<GiftCertificate> getAll(int page, int size);

    /**
     * Gets all Certificates with tags and optional filtering/sorting
     *
     * @param tagNames    Tag names to filter Certificates
     * @param partInfo    part info of name/desc to filter Certificates
     * @param sortColumns columns to sort of Certificates
     * @param orderTypes  sort order types
     * @param page page number of Certificates
     * @param size page size
     * @return List of sorted/filtered Certificates with Tags
     * @throws NoSuchEntityException      when Tag is not found
     * @throws InvalidParametersException when sort parameters are invalid
     */
    List<GiftCertificate> getAllWithTagsWithFilteringSorting(List<String> tagNames, String partInfo,
                                                                List<String> sortColumns, List<String> orderTypes,
                                                                int page, int size);

    /**
     * Gets Certificate by id.
     *
     * @param id Certificate id to search
     * @return founded Certificate
     * @throws NoSuchEntityException when Certificate is not found
     */
    GiftCertificate getById(long id);

    /**
     * Updates Certificate by id
     * with updating only fields that are passed
     *
     * @param id                 Certificate id to search
     * @param giftCertificate update information with Certificate fields or Tags
     * @return updated Certificate with Tags
     * @throws NoSuchEntityException  when Certificate is not found
     */
    GiftCertificate updateById(long id, GiftCertificate giftCertificate);

    /**
     * Deletes Certificate by id.
     *
     * @param id Certificate id to search
     * @throws NoSuchEntityException when Certificate is not found
     */
    void deleteById(long id);
}
