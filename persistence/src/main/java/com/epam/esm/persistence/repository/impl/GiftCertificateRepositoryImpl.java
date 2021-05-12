package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.model.entity.GiftCertificate;
import com.epam.esm.persistence.model.SortParamsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate>
        implements GiftCertificateRepository {

    @Autowired
    public GiftCertificateRepositoryImpl(EntityManager entityManager) {
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                            List<String> tagNames, String partInfo,
                                                            Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (tagNames != null) {
            predicates.add(buildPredicateByTagName(root, tagNames));
        }
        if (partInfo != null) {
            predicates.add(buildPredicateByPartInfo(root, partInfo));
        }
        if (!predicates.isEmpty()) {
            query.where(buildHelper.buildAndPredicates(predicates));
            if (tagNames != null) {
                query.groupBy(root.get("id"));
                query.having(builder.greaterThanOrEqualTo(builder.count(root), (long)tagNames.size()));
            }
        }

        if (sortParameters != null) {
            List<Order> orderList = buildHelper.buildOrderList(root, sortParameters);
            if (!orderList.isEmpty()) {
                query.orderBy(orderList);
            }
        }

        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private Predicate buildPredicateByTagName(Root<GiftCertificate> root, List<String> tagNames) {
        Join<GiftCertificate, Tag> tagsJoin = root.join("tags");

        return buildHelper.buildOrEqualPredicates(tagsJoin, "name", tagNames);
    }

    private Predicate buildPredicateByPartInfo(Root<GiftCertificate> root, String partInfo) {
        String regexValue = buildHelper.convertToRegex(partInfo);
        Predicate predicateByNameInfo = builder.like(root.get("name"), regexValue);
        Predicate predicateByDescriptionInfo = builder.like(root.get("description"), regexValue);

        return builder.or(predicateByNameInfo, predicateByDescriptionInfo);
    }
}
