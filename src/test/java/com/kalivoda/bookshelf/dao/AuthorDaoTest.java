package com.kalivoda.bookshelf.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kalivoda.bookshelf.exception.EntityNotFoundException;
import com.kalivoda.bookshelf.model.Author;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorDaoTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    public void testCreateAndGetAuthor() throws Exception {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setBirthYear(1980);

        //create author
        Author savedAuthor = authorDao.createAuthor(author);
        Assert.assertNotNull(savedAuthor.getId());
        Assert.assertEquals(author.getFirstName(), savedAuthor.getFirstName());
        Assert.assertEquals(author.getLastName(), savedAuthor.getLastName());
        Assert.assertEquals(author.getBirthYear(), savedAuthor.getBirthYear());

        //get created author
        Author retrievedAuthor = authorDao.getAuthor(savedAuthor.getId());
        Assert.assertNotNull(retrievedAuthor.getId());
        Assert.assertEquals(retrievedAuthor.getFirstName(), savedAuthor.getFirstName());
        Assert.assertEquals(retrievedAuthor.getLastName(), savedAuthor.getLastName());
        Assert.assertEquals(retrievedAuthor.getBirthYear(), savedAuthor.getBirthYear());
    }

    @Test
    public void testGetNotExistingAuthor() {
        try {
            authorDao.getAuthor(9999L);
            Assert.fail();
        } catch (EntityNotFoundException e) {
            //expected behavior
        }
    }
}