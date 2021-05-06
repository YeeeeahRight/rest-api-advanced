package com.epam.esm.service.logic;

import static org.mockito.Mockito.*;

import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.persistence.repository.impl.TagRepositoryImpl;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.tag.TagServiceImpl;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    @MockBean
    private TagRepositoryImpl tagDao;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private TagServiceImpl tagService;

    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() {
        when(tagDao.findByName(NAME)).thenReturn(Optional.empty());
        tagService.create(TAG);
        verify(tagDao).create(TAG);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateShouldThrowsDuplicateEntityExceptionWhenExist() {
        when(tagDao.findByName(NAME)).thenReturn(Optional.of(TAG));
        tagService.create(TAG);
    }

    @Test
    public void testGetAllShouldGetAll() {
        tagService.getAll(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        verify(tagDao).getAll(any());
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
