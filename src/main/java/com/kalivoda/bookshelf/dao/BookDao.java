package com.kalivoda.bookshelf.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kalivoda.bookshelf.exception.EntityNotFoundException;
import com.kalivoda.bookshelf.model.Author;
import com.kalivoda.bookshelf.model.Book;

@Repository
public class BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String CREATE_BOOK = "INSERT INTO book (title, abstract) VALUES (?, ?) RETURNING id";

    private static final String CREATE_BOOK_AUTHOR = "INSERT INTO book_author (author_id, book_id) VALUES (?,?)";

    private static final String GET_BOOK = "SELECT b.id as book_id, b.title, b.abstract, a.id as auth_id, a.firstname, a.lastname, a.birthyear " +
            "FROM book b, book_author ba, author a " +
            "WHERE b.id = ba.book_id AND ba.author_id = a.id " +
            "AND @@@FILTER_COLUMN@@@ = ? " +
            "ORDER BY b.id";

    /**
     * Creates a row in book table and then necessary rows in book_author depending on the number of book authors.
     * the method is transactional to ensure consistency.
     * @param book
     * @return created book with full representation of authors (embedded).
     */
    @Transactional
    public Book createBook(Book book) {
        Validate.isTrue(book.getAuthors() != null);
        Validate.isTrue(book.getAuthors().size() > 0);
        createBookInternal(book);
        createBookAuthor(book);
        return getBook(book.getId());
    }

    private void createBookInternal(Book book) {
        List<Object> args = new LinkedList<>();
        args.add(book.getTitle());
        args.add(book.getDescription());
        Long id = jdbcTemplate.queryForObject(CREATE_BOOK, args.toArray(), Long.class);
        book.setId(id);
    }

    /**
     * Retrieves a book with full representation of authors (embedded).
     */
    public Book getBook(long id) {
        List<Book> books = jdbcTemplate.query(GET_BOOK.replaceAll("@@@FILTER_COLUMN@@@", "b.id"), new Object[]{id}, new BookCollectionResultSetExtractor());
        if (books.size() != 1) {
            throw new EntityNotFoundException("No book found for given Id: " + id);
        }
        return books.get(0);
    }

    public List<Book> searchBooks(String title) {
        List<Book> books = jdbcTemplate.query(GET_BOOK.replaceAll("@@@FILTER_COLUMN@@@", "b.title"), new Object[]{title}, new BookCollectionResultSetExtractor());
        return books;
    }

    private void createBookAuthor(Book book) {
        Validate.notNull(book.getId());
        Iterator<Author> authorIterator = book.getAuthors().iterator();
        jdbcTemplate.batchUpdate(CREATE_BOOK_AUTHOR, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, authorIterator.next().getId());
                ps.setLong(2, book.getId());
            }

            @Override
            public int getBatchSize() {
                return book.getAuthors().size();
            }
        });
    }

    /**
     * The resultset combines the columns from book and author tables. There may be multiple lines for particular book (because book may have
     * multiple authors). We need to merge the rows and return one Book object with multiple Authors.
     */
    private class BookCollectionResultSetExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Book> books = new LinkedList<>();
            Set<Author> authors = new LinkedHashSet<>();
            Book currentBook = null;
            long id = 0;
            while (rs.next()) {
                if (rs.getLong(TableFieldsName.BookTable.BOOK_ID) != id) {
                    id = rs.getLong(TableFieldsName.BookTable.BOOK_ID);
                    if (currentBook != null) {
                        currentBook.setAuthors(authors);
                        books.add(currentBook);
                    }
                    currentBook = new Book();
                    currentBook.setId(rs.getLong(TableFieldsName.BookTable.BOOK_ID));
                    currentBook.setTitle(rs.getString(TableFieldsName.BookTable.TITLE));
                    currentBook.setDescription(rs.getString(TableFieldsName.BookTable.ABSTRACT));
                }
                Author author = new Author();
                author.setId(rs.getLong(TableFieldsName.AuthorTable.AUTHOR_ID));
                author.setFirstName(rs.getString(TableFieldsName.AuthorTable.FIRST_NAME));
                author.setLastName(rs.getString(TableFieldsName.AuthorTable.LAST_NAME));
                author.setBirthYear(rs.getInt(TableFieldsName.AuthorTable.BIRTH_YEAR));
                authors.add(author);
            }
            if (currentBook != null) {
                currentBook.setAuthors(authors);
                books.add(currentBook);
            }
            return books;
        }
    }
}
