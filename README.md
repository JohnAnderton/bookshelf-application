# Bookshelf application

Created REST endpoints:

| Path          | HTTP Method           | Description  |
| ------------- |:---------------------:| ------------|
`/author`                | `POST`    | creating of author |
`/author/{id}`           | `GET`     | author details with his books |
`/book`                  | `POST`    | create book (book must contain at least one author id) |
`/book/id`               | `POST`    | book details with authors |
`/book/?title={title}`   | `GET`     | searching books |



Used technologies and frameworks:
* Springboot
* Spring
* Jersey
* PostgreSQL
* Junit

Installation steps:
1. Specify spring datasource property `spring.datasource.url` for instance of PostgreSQL.
2. Run `src/main/resources/create_schema.sql` under admin role on PostgreSQL DB.
3. Run application via Springboot BookshelfApplication class

Tests:
1. Transactional tests for DAO layer.
2. REST end-to-end tests.


Todos which are coming to my mind currently are:

* handling of exceptions states and proper mapping to HTTP status codes
* pagination of collections
* separate db schema for tests
* automatic db schema population

## Example of REST calls:
POST
`/author`
```json
{
    "firstName": "Mark",
    "lastName": "Twain",
    "birthYear": 1835
}
```


POST
`/author`
```json
{
    "firstName": "Ernest",
    "lastName": "Hemingway",
    "birthYear": 1899
}
```

POST
`/author`
```json
{
    "firstName": "Agantha",
    "lastName": "Christie",
    "birthYear": 1890
}
```

POST
`/book`
```json
{
    "title": "The Old Man And The Sea",
    "abstract": "popular novel",
    "authors": [
        {
            "id": 2
        }
    ]
}
```

GET
`/book/1`
```json
{
    "id": 1,
    "title": "The Old Man And The Sea",
    "authors": [
        {
            "id": 2,
            "firstName": "Ernest",
            "lastName": "Hemingway",
            "birthYear": 1899
        }
    ],
    "abstract": "popular novel"
}
```

GET
`/book?title=The%20Old%20Man%20And%20The%20Sea`
```json
[
    {
        "id": 1,
        "title": "The Old Man And The Sea",
        "authors": [
            {
                "id": 2,
                "firstName": "Ernest",
                "lastName": "Hemingway",
                "birthYear": 1899
            }
        ],
        "abstract": "popular novel"
    }
]
```