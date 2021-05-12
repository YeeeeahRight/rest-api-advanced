package com.epam.esm.persistence.query;

import com.epam.esm.persistence.model.SortParamsContext;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CriteriaQueryBuildHelper {
    private static final String ANY_REGEX = "%";

    private final CriteriaBuilder builder;

    public CriteriaQueryBuildHelper(CriteriaBuilder criteriaBuilder) {
        this.builder = criteriaBuilder;
    }

    public Predicate buildAndPredicates(List<Predicate> predicates) {
        if (predicates == null || predicates.isEmpty()) {
            return null;
        }
        Predicate resultPredicate = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            resultPredicate = builder.and(resultPredicate, predicates.get(i));
        }
        return resultPredicate;
    }

    public <T> Predicate buildOrEqualPredicates(Path<T> root, String columnName, List<?> values) {
        int counter = 0;
        Predicate predicate = null;
        for (Object value : values) {
            Predicate currentPredicate = builder.equal(root.get(columnName), value);
            if (counter++ == 0) {
                predicate = currentPredicate;
            } else {
                predicate = builder.or(predicate, currentPredicate);
            }
        }

        return predicate;
    }

    public <T> List<Order> buildOrderList(Root<T> root, SortParamsContext sortParameters) {
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
                order = builder.asc(root.get(column));
            } else {
                order = builder.desc(root.get(column));
            }
            orderList.add(order);
        }
        return orderList;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptionalQueryResult(Query query) {
        try {
            T entity = (T) query.getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public String convertToRegex(String value) {
        return ANY_REGEX + value + ANY_REGEX;
    }
}
