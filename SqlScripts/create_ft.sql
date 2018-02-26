USE moviedb;
DROP TABLE IF EXISTS ft;

CREATE TABLE ft (
	id varchar(10),
	entry varchar(100)
);

INSERT INTO ft (id, entry)
SELECT id, title
FROM movies;

INSERT INTO ft (id, entry)
SELECT id, name
FROM stars;