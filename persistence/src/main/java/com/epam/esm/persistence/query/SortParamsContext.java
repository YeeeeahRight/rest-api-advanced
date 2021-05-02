package com.epam.esm.persistence.query;

import java.util.List;

public class SortParamsContext {
    private final List<String> sortColumns;
    private final List<String> orderTypes;

    public SortParamsContext(List<String> sortColumns, List<String> orderTypes) {
        this.sortColumns = sortColumns;
        this.orderTypes = orderTypes;
    }

    public List<String> getSortColumns() {
        return sortColumns;
    }

    public List<String> getOrderTypes() {
        return orderTypes;
    }
}
