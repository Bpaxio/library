insert into author (id, name, surname, country)
values (100000, 'AuthorTest', 'DoeTest', 'CountryTest'),
       (100001, 'Author1', 'Doe1', 'USA'),
       (100002, 'Author2', 'Doe2', 'GB'),
       (100003, 'TestName', 'TestSurname', 'TestCountry');

insert into genre (id, name)
values (100000, 'Novel'),
       (100001, 'Drama'),
       (100002, 'Science fiction'),
       (100003, 'TestGenre');

insert into book (id, name, publication_date, publishing_office, price, genre_id, author_id)
values (100000, 'Novel of Author1', 1999, 'testOffice', 999.99, 100000, 100001),
       (100001, 'Novel of Author2', 1998, 'testOffice', 959.99, 100000, 100002),
       (100002, 'Novel of TestAuthor', 1998, 'testOffice', 899.99, 100000, 100000),
       (100003, 'Science fiction of TestAuthor', 1997, 'testOffice', 859.99, 100002, 100000),
       (100004, 'Drama of TestAuthor', 1996, 'testOffice', 799.99, 100001, 100000);

insert into comment (id, author_username, created, message, book_id)
values (100000, 'TestCommentator1', '2019-02-27 14:09:23.356', 'testComment1', 100004),
       (100001, 'TestCommentator1', '2019-02-27 14:09:27.356', 'testComment2', 100004),
       (100002, 'TestCommentator2', '2019-02-27 14:09:23.376', 'testComment3', 100004),
       (100003, 'TestCommentator2', '2019-02-27 14:09:29.356', 'testComment4', 100004),
       (100004, 'TestCommentator1', '2019-02-27 14:15:23.356', 'testComment5', 100004),
       (100005, 'TestCommentator0', '2019-02-27 19:15:23.356', 'testComment6', 100000);