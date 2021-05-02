package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GiftCertificateValidator implements Validator<GiftCertificateDto> {
    private static final int NAME_MAX_LENGTH = 80;
    private static final int NAME_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ONE;
    private static final BigDecimal PRICE_MAX_VALUE = new BigDecimal(Integer.MAX_VALUE);
    private static final int DURATION_MIN_VALUE = 1;

    @Override
    public boolean isValid(GiftCertificateDto giftCertificateDto) {
        return isNameValid(giftCertificateDto.getName()) &&
                isDescriptionValid(giftCertificateDto.getDescription()) &&
                isPriceValid(giftCertificateDto.getPrice()) &&
                isDurationValid(giftCertificateDto.getDuration());
    }

    public boolean isNameValid(String name) {
        if (name == null) {
            return false;
        }
        return name.length() >= NAME_MIN_LENGTH && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean isDescriptionValid(String description) {
        if (description == null) {
            return false;
        }
        return description.length() >= DESCRIPTION_MIN_LENGTH
                && description.length() <= DESCRIPTION_MAX_LENGTH;
    }

    public boolean isPriceValid(BigDecimal price) {
        if (price == null) {
            return false;
        }
        return price.compareTo(PRICE_MIN_VALUE) >= 0 &&
                price.compareTo(PRICE_MAX_VALUE) <= 0;
    }

    public boolean isDurationValid(int duration) {
        return duration >= DURATION_MIN_VALUE;
    }
}
