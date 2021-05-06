package com.epam.esm.persistence.query;

public final class NativeQuery {
    public static final String MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY =
            "SELECT t.id as 'id', t.name as 'tagName', MAX(o.cost) as 'highestCost'\n" +
                    "FROM orders o\n" +
                    "JOIN certificates_tags ct on o.certificate_id = ct.certificate_id\n" +
                    "JOIN tags t on ct.tag_id = t.id\n" +
                    "WHERE o.user_id = :userId\n" +
                    "GROUP BY t.id\n" +
                    "ORDER BY COUNT(t.id) DESC, MAX(o.cost) DESC\n" +
                    "LIMIT 1";
}