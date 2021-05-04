package com.epam.esm.persistence.query;

public final class NativeQuery {
    public static final String MOST_WIDELY_USED_WITH_HIGHEST_ORDER_COST_TAG_QUERY =
            "SELECT tags.id as 'id', tags.name as 'tagName', MAX(o.cost) AS 'highestCost'\n" +
            "FROM tags\n" +
            "JOIN certificates_tags ct on tags.id = ct.tag_id\n" +
            "JOIN orders o on ct.certificate_id = o.certificate_id\n" +
            "WHERE o.user_id = :userId\n" +
            "GROUP BY tags.id\n" +
            "ORDER BY COUNT(tags.id) DESC, MAX(o.cost) DESC\n" +
            "LIMIT 1;";
}
