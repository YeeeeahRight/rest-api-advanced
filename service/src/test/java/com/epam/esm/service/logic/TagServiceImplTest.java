package com.epam.esm.service.logic;

import static org.mockito.Mockito.*;

import com.epam.esm.persistence.repository.impl.TagRepositoryImpl;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.TagDtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.tag.TagServiceImpl;
import com.epam.esm.service.validator.TagValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
//сколько %

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TagServiceImpl.class})
public class TagServiceImplTest {
    private static final long ID = 1;
    private static final String NAME = "tag";
    private static final Tag TAG = new Tag(ID,NAME);
    private static final TagDto TAG_DTO = new TagDto(ID, NAME);

    @MockBean
    private TagRepositoryImpl tagDao;
    @MockBean
    private TagValidator tagValidator;
    @MockBean
    private TagDtoConverter tagDtoConverter;

    @Autowired
    private TagServiceImpl tagService;

    @Before
    public void init() {
        when(tagDtoConverter.convertToDto(TAG)).thenReturn(TAG_DTO);
        when(tagDtoConverter.convertToEntity(TAG_DTO)).thenReturn(TAG);
    }


    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() {
        when(tagValidator.isValid(TAG_DTO)).thenReturn(true);
        when(tagDao.findByName(NAME)).thenReturn(Optional.empty());
        tagService.create(TAG_DTO);
        verify(tagDao).create(TAG);
    }

    @Test(expected = InvalidEntityException.class)
    public void testCreateShouldThrowsInvalidEntityExceptionWhenInvalid() {
        when(tagValidator.isValid(TAG_DTO)).thenReturn(false);
        tagService.create(TAG_DTO);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateShouldThrowsDuplicateEntityExceptionWhenExist() {
        when(tagValidator.isValid(TAG_DTO)).thenReturn(true);
        when(tagDao.findByName(NAME)).thenReturn(Optional.of(TAG));
        tagService.create(TAG_DTO);
    }

    @Test
    public void testGetAllShouldGetAll() {
        tagService.getAll();
        verify(tagDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(tagDao.findById(ID)).thenReturn(Optional.of(TAG));
        tagService.getById(ID);
        verify(tagDao).findById(ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowsNotSuchEntityExceptionWhenNotFound() {
        when(tagDao.findById(ID)).thenReturn(Optional.empty());
        tagService.getById(ID);
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(tagDao.findById(ID)).thenReturn(Optional.of(TAG));
        tagService.deleteById(ID);
        verify(tagDao).deleteById(ID);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(tagDao.findById(ID)).thenReturn(Optional.empty());
        tagService.deleteById(ID);
    }
}
