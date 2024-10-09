INSERT INTO book (isbn, title,average) VALUES
('9780060935467', 'To Kill a Mockingbird',0),
('9780451524935', '1984',0),
('9780141439518', 'Pride and Prejudice',0),
('9781594631931', 'The Kite Runner',0),
('9780385490818', 'The Handmaid''s Tale',0),
('9780345339683', 'The Hobbit',0);

INSERT INTO tag (name) VALUES
('Classic'),
('Dystopian'),
('Romance'),
('Historical'),
('Fantasy'),
('Literary Fiction'),
('American'),
('British'),
('Young Adult'),
('Drama');

INSERT INTO book_tag (book_id, tag_id) VALUES
(1, 1),  -- 'To Kill a Mockingbird'에 'Classic' 태그를 연결
(1, 7),  -- 'To Kill a Mockingbird'에 'American' 태그를 연결
(2, 2),  -- '1984'에 'Dystopian' 태그를 연결
(2, 7),  -- '1984'에 'American' 태그를 연결
(2, 8),  -- '1984'에 'British' 태그를 연결
(3, 3),  -- 'Pride and Prejudice'에 'Romance' 태그를 연결
(3, 8),  -- 'Pride and Prejudice'에 'British' 태그를 연결
(4, 4),  -- 'The Kite Runner'에 'Historical' 태그를 연결
(5, 5),  -- 'The Handmaid''s Tale'에 'Fantasy' 태그를 연결
(6, 5),  -- 'The Hobbit'에 'Fantasy' 태그를 연결
(6, 9);  -- 'The Hobbit'에 'Young Adult' 태그를 연결

INSERT INTO review (book_id, rating, content) VALUES
(1, 5, 'Amazing book! A must-read for everyone.'),
(1, 4, 'Really enjoyed the depth of the characters and the storyline.'),
(1, 3, 'It was good, but the pacing was a bit slow for my taste.'),
(2, 2, 'Not what I expected. The plot was predictable.'),
(2, 1, 'Did not enjoy the book. Found it quite boring.');