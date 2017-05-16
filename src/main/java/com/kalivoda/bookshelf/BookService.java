package com.kalivoda.bookshelf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalivoda.bookshelf.dao.BookDao;
import com.kalivoda.bookshelf.model.Book;

@Service
public class BookService {

    @Autowired
    BookDao bookDao;

    public Book createBook(Book book) {
        return bookDao.createBook(book);
    }

    public Book getBook(long id) {
        return bookDao.getBook(id);
    }

    public List<Book> searchBooks(String title) {
        return bookDao.searchBooks(title);
    }
}
