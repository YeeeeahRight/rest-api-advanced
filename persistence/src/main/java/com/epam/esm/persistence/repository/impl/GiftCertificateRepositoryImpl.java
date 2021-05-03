package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.AbstractRepository;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.query.SortParamsContext;
import org.springframework.beans.factory.annotation.Autowired;
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
        super(entityManager);
    }

    @Override
    protected Class<GiftCertificate> getEntityType() {
        return GiftCertificate.class;
    }

    @Override
    public List<GiftCertificate> getAllWithSorting(SortParamsContext sortParameters) {
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);

        List<Order> orderList = buildOrderList(builder, root, sortParameters);
        query.orderBy(orderList);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<GiftCertificate> getAllWithFiltering(String tagName, String partInfo) {
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);

        Predicate[] predicates = new Predicate[2];
        if (tagName != null) {
            predicates[0] = buildPredicateByTagName(root, builder, tagName);
        }
        if (partInfo != null) {
            predicates[1] = buildPredicateByPartInfo(root, builder, partInfo);
        }
        query.where(builder.and(predicates));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                            String tagName, String partInfo) {
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);

        Predicate[] predicates = new Predicate[2];
        if (tagName != null) {
            predicates[0] = buildPredicateByTagName(root, builder, tagName);
        }
        if (partInfo != null) {
            predicates[1] = buildPredicateByPartInfo(root, builder, partInfo);
        }
        query.where(builder.and(predicates));

        List<Order> orderList = buildOrderList(builder, root, sortParameters);
        query.orderBy(orderList);

        return entityManager.createQuery(query).getResultList();
    }

    private Predicate buildPredicateByTagName(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder,
                                              String tagName) {
        Join<GiftCertificate, Tag> tagsJoin = root.join("tags");
        return criteriaBuilder.equal(tagsJoin.get("name"), tagName);
    }

    private Predicate buildPredicateByPartInfo(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder,
                                         String partInfo) {
        partInfo = "%" + partInfo + "%";
        Predicate predicateByNameInfo = criteriaBuilder.like(root.get("name"), partInfo);
        Predicate predicateByDescriptionInfo = criteriaBuilder.like(root.get("description"), partInfo);
        return criteriaBuilder.or(predicateByNameInfo, predicateByDescriptionInfo);
    }

    private List<Order> buildOrderList(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                      SortParamsContext sortParameters) {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < sortParameters.getSortColumns().size(); i++) {
            String column = sortParameters.getSortColumns().get(i);
            String orderType;
            if (sortParameters.getOrderTypes().size() > i) {
                orderType = sortParameters.getOrderTypes().get(i);
            } else {
                orderType = "ASC";
            }
            Order order;
            if (orderType.equalsIgnoreCase("ASC")) {
                order = criteriaBuilder.asc(root.get(column));
            } else {
                order = criteriaBuilder.desc(root.get(column));
            }
            orderList.add(order);
        }
        return orderList;
    }
}
