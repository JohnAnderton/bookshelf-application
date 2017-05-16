package com.kalivoda.bookshelf.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kalivoda.bookshelf.BookService;
import com.kalivoda.bookshelf.model.Book;

@Component
@Path("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GET
    @Produces("application/json")
    public Response searchBooks(@QueryParam("title") String title) {
        if (title == null) {
            //TODO add listing of all books
            //solve pagination
            //bookService.getAllBooks();
            throw new BadRequestException("listing of all books is not supported yet, query parameter 'title' is mandatory at the moment");
        }
        List<Book> books = bookService.searchBooks(title);
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getBook(@PathParam("id") long id) {
        Book book = bookService.getBook(id);
        return Response.ok(book).build();
    }

    @POST
    @Produces("application/json")
    public Response createBook(Book book) {
        if (book.getId() != null) {
            throw new BadRequestException("Entity to be created may not contain id");
        }
        if (book.getAuthors() == null || book.getAuthors().size() == 0) {
            throw new BadRequestException("Entity book must have at least one author");
        }
        Book createdBook = bookService.createBook(book);
        return Response.ok(createdBook).status(Response.Status.CREATED).build();
    }
}
