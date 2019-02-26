insert into author (id, name, surname, country)
values (100000, 'AuthorTest', 'DoeTest', 'CountryTest'),
       (100001, 'Author1', 'Doe1', 'USA'),
       (100002, 'Author2', 'Doe2', 'GB'),
       (100003, 'TestName', 'TestSurname', 'TestCountry');

insert into genre (id, name)
values (100000, 'Novel'),
       (100001, 'Drama'),
       (100002, 'Science fiction');

insert into book (id, name, publication_date, publishing_office, price, genre_id, author_id)
values (100000, 'Novel of Author1', 1999, 'testOffice', 999.99, 100000, 100001),
       (100001, 'Novel of Author2', 1998, 'testOffice', 959.99, 100000, 100002),
       (100002, 'Novel of TestAuthor', 1998, 'testOffice', 899.99, 100000, 100000),
       (100003, 'Science fiction of TestAuthor', 1997, 'testOffice', 859.99, 100002, 100000),
       (100004, 'Drama of TestAuthor', 1996, 'testOffice', 799.99, 100001, 100000);