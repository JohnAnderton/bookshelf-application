package com.kalivoda.bookshelf.dao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kalivoda.bookshelf.exception.EntityNotFoundException;
import com.kalivoda.bookshelf.model.Author;
import com.kalivoda.bookshelf.model.Book;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookDaoTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;

    private Author author1;
    private Author author2;

    @Test
    public void testCreateBook() throws Exception {
        //save author first
        initializeAuthors();

        Book book = new Book();
        book.setTitle("Adventures of Huckleberry Finn");
        book.setDescription("Adventures of Huckleberry Finn (or, in more recent editions, The Adventures of Huckleberry Finn) is a novel by Mark Twain, first published in the United Kingdom in December 1884 and in the United States in February 1885.");
        book.setAuthors(new HashSet<>(Arrays.asList(author1, author2)));

        Book savedBook = bookDao.createBook(book);
        Assert.assertEquals(book.getTitle(), savedBook.getTitle());
        Assert.assertEquals(book.getDescription(), savedBook.getDescription());
        Assert.assertEquals(book.getAuthors(), savedBook.getAuthors());

        //retrieve book
        Book retrievedBook = bookDao.getBook(savedBook.getId());
        Assert.assertEquals(savedBook.getTitle(), retrievedBook.getTitle());
        Assert.assertEquals(savedBook.getDescription(), retrievedBook.getDescription());
        Assert.assertEquals(savedBook.getAuthors(), retrievedBook.getAuthors());
    }

    @Test
    public void testGetNonExistingBook() {
        try {
            bookDao.getBook(9999L);
            Assert.fail();
        } catch (EntityNotFoundException e) {
            //expected behavior
        }
    }

    @Test
    public void testCreateBookWithoutAuthors() {
        Book book = new Book();
        book.setTitle("English Grammar");
        book.setDescription("very brief description of english grammar");
        try {
            bookDao.createBook(book);
            Assert.fail("should have failed, because there are no authors for this book");
        } catch (IllegalArgumentException e) {
            //expected behavior
        }
    }

    @Test
    public void testSearchBooks() {
        initializeAuthors();

        Book book1 = new Book("book1", "abstract1", new HashSet<>(Arrays.asList(author1, author2)));
        Book book2 = new Book("book1", "abstract1", new HashSet<>(Arrays.asList(author1)));
        bookDao.createBook(book1);
        bookDao.createBook(book2);

        List<Book> books = bookDao.searchBooks("book1");
        Assert.assertEquals(2, books.size());
    }

    private void initializeAuthors() {
        author1 = new Author();
        author1.setFirstName("Mark");
        author1.setLastName("Twain");
        author1.setBirthYear(1835);

        author2 = new Author();
        author2.setFirstName("Ernest");
        author2.setLastName("Hemingway");
        author2.setBirthYear(1899);

        authorDao.createAuthor(author1);
        authorDao.createAuthor(author2);
    }
}