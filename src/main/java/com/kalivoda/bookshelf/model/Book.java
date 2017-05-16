package com.kalivoda.bookshelf.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    private Long id;

    private String title;

    @JsonProperty(value = "abstract")
    private String description;

    //example of solution via ORM
//    @ManyToMany(fetch = FetchType.EAGER, cascade={ CascadeType.PERSIST, CascadeType.MERGE })
//    @JoinTable(
//            name = "BOOK_AUTHOR",
//            joinColumns = @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID"))
    private Set<Author> authors;

    public Book() {
    }

    public Book(String title, String description, Set<Author> authors) {
        this.title = title;
        this.description = description;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
