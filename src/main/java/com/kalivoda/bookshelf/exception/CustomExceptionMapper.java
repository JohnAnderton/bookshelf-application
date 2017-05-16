package com.kalivoda.bookshelf.exception;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

    @Provider
    public class CustomExceptionMapper implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            if (exception instanceof BadRequestException) {
                return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage())
                        .type("text/plain").build();
            } else if (exception instanceof EntityNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage())
                        .type("text/plain").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
                    .type("text/plain").build();
        }
    }
