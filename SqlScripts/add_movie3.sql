DELIMITER $$
DROP PROCEDURE IF EXISTS add_movie;
CREATE PROCEDURE add_movie(IN Atitle varchar(100), IN Ayear INT, IN Adirector varchar(100), 
    IN Astar_name varchar(100), IN Agenre_name varchar(32), OUT message varchar(500), OUT ID INT)
BEGIN
    
    
    DECLARE mCHARS varchar(2);
    DECLARE mINT INT(7) ZEROFILL;
    DECLARE new_mid varchar(10);
    DECLARE sCHARS varchar(2);
    DECLARE sINT INT(7) ZEROFILL;
    DECLARE new_sid varchar(10);
    DECLARE new_gid INT;
    

    DECLARE movie_number varchar(10);
    DECLARE star_number varchar(10);
    DECLARE genre_number INT;
    SELECT LEFT(movie_id, 2), LEFT(star_id,2), genre_id, RIGHT(movie_id,7), RIGHT(star_id, 7)
           INTO mCHARS, sCHARS, new_gid, mINT, sINT
    FROM maximum WHERE movie_id = movie_id AND star_id = star_id AND genre_id = genre_id; 
    
    DELETE FROM maximum WHERE genre_id = new_gid; 
    SET mINT = mINT+1;
    SET sINT = sINT+1;
    SET new_gid = new_gid+1;
    SET new_mid = CONCAT(mCHARS,mINT);
    SET new_sid = CONCAT(sCHARS,sINT);
    INSERT INTO maximum (movie_id,star_id,genre_id) VALUES (new_mid,new_sid,new_gid);
    
    SET message = "";
    SELECT id INTO movie_number FROM movies WHERE id = id AND title = Atitle AND year = Ayear AND director = Adirector LIMIT 1;

    IF movie_number IS NULL
    THEN
        INSERT INTO movies(id,title,year,director) VALUES (new_mid,Atitle,Ayear,Adirector);
        SET message = CONCAT(message,"The movie was not found. Adding new movie to database with id=",new_mid);
    ELSE
        SET new_mid = movie_number;
    END IF;
    
    SELECT id INTO star_number FROM stars WHERE id = id AND name = Astar_name LIMIT 1;
    IF star_number IS NULL
    THEN
        SET message = CONCAT(message,"The star was not found. Inserting star into table with id=",new_sid);
        INSERT INTO stars(id,name,birthYear) VALUES (new_sid,Astar_name, NULL);
    ELSE 
        SET new_sid = star_number;
    END IF;

    SET message = CONCAT(message,"Adding stars_in_movies relation (",new_sid,", ",new_mid,")");
    INSERT INTO stars_in_movies(starId,movieId) VALUES (new_sid,new_mid);
    
    SELECT id INTO genre_number FROM genres WHERE id = id AND name = Agenre_name LIMIT 1;
    IF genre_number IS NULL
    THEN
        SET message = CONCAT(message,"The genre was not found. Inserting genre into table with id=",new_gid);
        INSERT INTO genres(id,name) VALUES (new_gid,Agenre_name);
    ELSE
        SET new_gid = genre_number;
    END IF; 
    SET message = CONCAT(message,"Adding genres_in_movies relation (",new_gid,", ",new_mid,")");
    INSERT INTO genres_in_movies(genreId,movieId) VALUES (new_gid,new_mid);
    SET ID = 1;
END
$$

DELIMITER ;