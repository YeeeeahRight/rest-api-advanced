package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.query.SortParamsContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SortParamsContextValidator implements Validator<SortParamsContext> {
    private static final List<String> ORDER_TYPES = Arrays.asList("ASC", "DESC");
    private static final List<String> CERTIFICATE_FIELD_NAMES = new ArrayList<>();

    static {
        Arrays.stream(GiftCertificate.class.getDeclaredFields()).
                forEach(field -> CERTIFICATE_FIELD_NAMES.add(field.getName()));
    }

    @Override
    public boolean isValid(SortParamsContext item) {
        return CERTIFICATE_FIELD_NAMES.containsAll(item.getSortColumns())
                && item.getOrderTypes().stream().allMatch(order -> ORDER_TYPES.contains(order.toUpperCase()));
    }
}
