package com.epam.esm.persistence.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortParamsContext that = (SortParamsContext) o;

        if (!Objects.equals(sortColumns, that.sortColumns)) return false;
        return Objects.equals(orderTypes, that.orderTypes);
    }

    @Override
    public int hashCode() {
        int result = sortColumns != null ? sortColumns.hashCode() : 0;
        result = 31 * result + (orderTypes != null ? orderTypes.hashCode() : 0);
        return result;
    }
}
