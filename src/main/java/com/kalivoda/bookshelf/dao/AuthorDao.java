package com.kalivoda.bookshelf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.kalivoda.bookshelf.exception.EntityNotFoundException;
import com.kalivoda.bookshelf.model.Author;
import com.kalivoda.bookshelf.model.Book;

@Repository
public class AuthorDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String CREATE_SQL = "INSERT INTO author (firstname, lastname, birthyear) VALUES (?, ?, ?) RETURNING id";

    private static final String GET_AUTHOR = "SELECT a.id as auth_id, a.firstname, a.lastname, a.birthyear, b.id as book_id, b.title, b.abstract " +
            " FROM author a LEFT JOIN book_author ba ON (a.id = ba.author_id) LEFT JOIN book b ON (ba.book_id = b.id) " +
            " WHERE a.id = ?" +
            " ORDER BY a.id";

    public Author createAuthor(Author author) {
        List<Object> args = new LinkedList<>();
        args.add(author.getFirstName());
        args.add(author.getLastName());
        args.add(author.getBirthYear());

        Long id = jdbcTemplate.queryForObject(CREATE_SQL, args.toArray(), Long.class);
        author.setId(id);
        return author;
    }

    public Author getAuthor(long id) {
        Author author = jdbcTemplate.query(GET_AUTHOR, new Object[]{id}, new AuthorResultSetExtractor(id));
        return author;
    }

    private class AuthorResultSetExtractor implements ResultSetExtractor<Author> {

        private long id;

        public AuthorResultSetExtractor(long id) {
            this.id = id;
        }

        @Override
        public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
            Author author = null;
            Set<Book> books = new LinkedHashSet<>();
            while (rs.next()) {
                if (author == null) {
                    author = new Author();
                    author.setId(rs.getLong(TableFieldsName.AuthorTable.AUTHOR_ID));
                    author.setFirstName(rs.getString(TableFieldsName.AuthorTable.FIRST_NAME));
                    author.setLastName(rs.getString(TableFieldsName.AuthorTable.LAST_NAME));
                    author.setBirthYear(rs.getInt(TableFieldsName.AuthorTable.BIRTH_YEAR));
                }
                if (rs.getLong(TableFieldsName.BookTable.BOOK_ID) != 0) {
                    Book book = new Book();
                    book.setId(rs.getLong(TableFieldsName.BookTable.BOOK_ID));
                    book.setTitle(rs.getString(TableFieldsName.BookTable.TITLE));
                    book.setDescription(rs.getString(TableFieldsName.BookTable.ABSTRACT));
                    books.add(book);
                }
            }
            if (author == null) {
                    throw new EntityNotFoundException("No author found with given Id: " + id);
            }
            author.setBooks(books);
            return author;
        }
    }
}
