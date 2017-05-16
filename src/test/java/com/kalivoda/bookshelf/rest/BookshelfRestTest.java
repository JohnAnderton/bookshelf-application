package com.kalivoda.bookshelf.rest;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.kalivoda.bookshelf.model.Author;
import com.kalivoda.bookshelf.model.Book;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookshelfRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createAuthorTest() {
        Author author = new Author("John", "Doe", 1950);
        Author createdAuthor = restTemplate.postForObject("/author", author, Author.class);

        Assert.assertNotNull(createdAuthor.getId());
        Assert.assertEquals(author.getFirstName(), createdAuthor.getFirstName());
        Assert.assertEquals(author.getLastName(), createdAuthor.getLastName());
        Assert.assertEquals(author.getBirthYear(), createdAuthor.getBirthYear());
        Assert.assertEquals(author.getBooks(), createdAuthor.getBooks());

        Author retrievedAuthor = restTemplate.getForObject("/author/{id}", Author.class, createdAuthor.getId());
        Assert.assertEquals(createdAuthor.getId(), retrievedAuthor.getId());
        Assert.assertEquals(createdAuthor.getFirstName(), retrievedAuthor.getFirstName());
        Assert.assertEquals(createdAuthor.getLastName(), retrievedAuthor.getLastName());
        Assert.assertEquals(createdAuthor.getBirthYear(), retrievedAuthor.getBirthYear());
    }

    @Test
    public void creteNotValidAuthorTest() {
        //try to create author without name
        Author author = new Author(null, "Doe", 1950);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/author", author, String.class);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        //try to create author containing Id
        author = new Author("John", "Doe", 1950);
        author.setId(222L);
        responseEntity = restTemplate.postForEntity("/author", author, String.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertEquals("Entity to be created may not contain id", responseEntity.getBody());
    }

    @Test
    public void createBookTest() {
        Author author = new Author("Karel", "May", 1950);
        author = restTemplate.postForObject("/author", author, Author.class);

        Book book = new Book("Vinnetou", "some abstract text", new HashSet<>(Arrays.asList(author)));
        Book createdBook = restTemplate.postForObject("/book", book, Book.class);

        Assert.assertNotNull(createdBook.getId());
        Assert.assertEquals(createdBook.getTitle(), book.getTitle());
        Assert.assertEquals(createdBook.getDescription(), book.getDescription());
        Assert.assertEquals(createdBook.getAuthors(), book.getAuthors());
    }

    @Test
    public void createBookWithNoAuthorTest() {
        Book book = new Book("Vinnetou", "some abstract text", new HashSet<>());
        ResponseEntity<String> bookResponseEntity = restTemplate.postForEntity("/book", book, String.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, bookResponseEntity.getStatusCode());
    }
}
