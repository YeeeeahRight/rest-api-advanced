package com.epam.esm.service.dto.converter;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    @Override
    public User convertToEntity(UserDto dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setName(dto.getName());

        return user;
    }

    @Override
    public UserDto convertToDto(User entity) {
        UserDto userDto = new UserDto();

        userDto.setId(entity.getId());
        userDto.setName(entity.getName());

        return userDto;
    }
}
