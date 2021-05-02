package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.query.SortParamsContext;

import java.util.List;

/**
 * Repository interface for Certificate
 */
public interface GiftCertificateRepository extends EntityRepository<GiftCertificate> {

    /**
     * Gets all Certificates with sorting.
     *
     * @param sortParameters sort parameters
     * @return List of sorted Certificates
     */
    List<GiftCertificate> getAllWithSorting(SortParamsContext sortParameters);

    /**
     * Gets all Certificates with filtering.
     *
     * @param tagName  tag name to filter
     * @param partInfo part info of name/description of Certificate to filter
     * @return List of filtered Certificates
     */
    List<GiftCertificate> getAllWithFiltering(String tagName, String partInfo);

    /**
     * Gets all Certificates with sorting and filtering.
     *
     * @param sortParameters sort parameters
     * @param tagName  tag name to filter
     * @param partInfo part info of name/description of Certificate to filter
     * @return List of filtered and sorted Certificates
     */
    List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                     String tagName, String partInfo);
}
