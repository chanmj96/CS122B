USE moviedb;
DROP TABLE IF EXISTS ft;

CREATE TABLE ft (
	id varchar(10),
	entry varchar(100),
	PRIMARY KEY (id),
	FULLTEXT(entry)
) ENGINE=MyISAM;

INSERT INTO ft (id, entry)
SELECT id, title
FROM movies;

INSERT INTO ft (id, entry)
SELECT id, name
FROM stars;