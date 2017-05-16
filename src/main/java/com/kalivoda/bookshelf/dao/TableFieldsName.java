package com.kalivoda.bookshelf.dao;

public interface TableFieldsName {

    interface BookTable {
        String BOOK_ID = "book_id";
        String ABSTRACT = "abstract";
        String TITLE = "title";
    }

    interface AuthorTable {
        String AUTHOR_ID = "auth_id";
        String FIRST_NAME = "firstname";
        String LAST_NAME = "lastname";
        String BIRTH_YEAR = "birthyear";
    }
}
