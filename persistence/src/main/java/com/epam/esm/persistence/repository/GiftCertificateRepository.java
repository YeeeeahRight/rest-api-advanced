package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.SortParamsContext;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository interface for Certificate
 */
public interface GiftCertificateRepository extends EntityRepository<GiftCertificate> {

    /**
     * Gets all Certificates with sorting and filtering.
     *
     * @param sortParameters sort parameters
     * @param tagNames  Tag names to filter
     * @param partInfo part info of name/description of Certificate to filter
     * @param pageable object with pagination info(page number, page size)
     * @return List of filtered and sorted Certificates
     */
    List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                     List<String> tagNames, String partInfo,
                                                     Pageable pageable);
}
