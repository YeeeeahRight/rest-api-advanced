package com.epam.esm.web.dto.converter;

//TODO Use ModelMapper for converters

/**
 * The interface DtoConverter
 *
 * @param <S> SourceEntity type
 * @param <D> DtoEntity type
 */
public interface DtoConverter<S, D> {
    /**
     * Converts Dto to source entity
     *
     * @param dto Dto entity to convert
     * @return converted source entity
     */
    S convertToEntity(D dto);

    /**
     * Converts source entity to Dto
     *
     * @param entity source entity to convert
     * @return converted dto entity
     */
    D convertToDto(S entity);
}
