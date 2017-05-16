package com.kalivoda.bookshelf;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.kalivoda.bookshelf.exception.CustomExceptionMapper;
import com.kalivoda.bookshelf.rest.AuthorController;
import com.kalivoda.bookshelf.rest.BookController;

@Configuration
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(AuthorController.class);
        register(BookController.class);

        register(CustomExceptionMapper.class);
    }
}

