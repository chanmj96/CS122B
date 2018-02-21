
DROP TABLE IF EXISTS maximum;
CREATE TABLE maximum(movie_id varchar(10) NOT NULL, star_id varchar(10) NOT NULL, genre_id INT NOT NULL);

DROP PROCEDURE IF EXISTS update_max;
DELIMITER $$
CREATE PROCEDURE update_max()
BEGIN
    DECLARE mid VARCHAR(10);
    DECLARE sid VARCHAR(10);
    DECLARE gid INT;

    SELECT max(id) INTO mid
    FROM movies WHERE id = id;

    SELECT max(id) INTO sid
    FROM stars WHERE id = id;

    SELECT max(id) INTO gid
    FROM genres WHERE id = id;

    INSERT INTO maximum(movie_id,star_id,genre_id) VALUES (mid,sid,gid);
END
$$
DELIMITER ;

CALL update_max();
