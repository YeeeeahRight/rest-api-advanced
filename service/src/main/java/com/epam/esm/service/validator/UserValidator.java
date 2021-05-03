package com.epam.esm.service.validator;

import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator<UserDto> {
    private static final int NAME_MAX_LENGTH = 60;
    private static final int NAME_MIN_LENGTH = 1;

    @Override
    public boolean isValid(UserDto entity) {
        String name = entity.getName();
        if (name == null) {
            return false;
        }
        int nameLength = name.length();

        return nameLength >= NAME_MIN_LENGTH && nameLength <= NAME_MAX_LENGTH;
    }
}
