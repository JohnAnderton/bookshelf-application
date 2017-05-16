package com.kalivoda.bookshelf.rest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kalivoda.bookshelf.dao.AuthorDao;
import com.kalivoda.bookshelf.model.Author;

@Component
@Path("/author")
public class AuthorController {

    @Autowired
    private AuthorDao authorDao;

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getAuthor(@PathParam("id") Long id) {
        Author author = authorDao.getAuthor(id);
        return Response.ok(author).build();
    }

    @POST
    @Produces("application/json")
    public Response createAuthor(Author author) {
        if (author.getId() != null) {
            throw new BadRequestException("Entity to be created may not contain id");
        }
        authorDao.createAuthor(author);
        return Response.ok(author).status(Response.Status.CREATED).build();
    }
}
