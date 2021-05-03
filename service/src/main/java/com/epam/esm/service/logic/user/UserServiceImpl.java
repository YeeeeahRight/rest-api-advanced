package com.epam.esm.service.logic.user;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.UserDtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;
    private final Validator<UserDto> userValidator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDtoConverter userDtoConverter,
                           Validator<UserDto> userValidator) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
        this.userValidator = userValidator;
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (!userValidator.isValid(userDto)) {
            throw new InvalidEntityException("user.invalid");
        }
        User user = userDtoConverter.convertToEntity(userDto);
        user = userRepository.create(user);
        return userDtoConverter.convertToDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.getAll();
        return users.stream()
                .map(userDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("user.not.found");
        }
        return userDtoConverter.convertToDto(userOptional.get());
    }
}
