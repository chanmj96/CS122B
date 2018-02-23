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
    
    DECLARE tempStarId varchar(10);
    DECLARE tempGenreId varchar(10);
    
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

    
    IF (SELECT EXISTS (SELECT 1 FROM movies WHERE title=Atitle))
    THEN
    		SELECT id INTO new_mid FROM movies WHERE title=Atitle;
    		IF Ayear IS NOT NULL
    		THEN
    			UPDATE movies SET year=Ayear WHERE title=Atitle;
    		END IF;
    		IF Adirector IS NOT NULL
    		THEN
    			UPDATE movies SET director=Adirector WHERE title=Atitle;
    		END IF;
    ELSE

    		INSERT INTO movies(id, title, year, director) VALUES (new_mid, Atitle, Ayear, Adirector);
    		SET message = CONCAT(message,"The movie was not found. Adding new movie to database with id=",new_mid);
	END IF;


	
    IF (EXISTS (SELECT 1 FROM stars WHERE name=Astar_name LIMIT 1))
    THEN
    		SELECT id INTO tempStarId FROM stars WHERE name=Astar_name;
		IF (NOT EXISTS (SELECT * FROM stars_in_movies WHERE starId=tempStarId))
		THEN
			INSERT INTO stars_in_movies(starId,movieId) VALUES (tempStarId,new_mid);
		END IF;
	ELSE
	    SET message = CONCAT(message,"The star was not found. Inserting star into table with id=",new_sid);
        INSERT INTO stars(id,name,birthYear) VALUES (new_sid,Astar_name, NULL);
        SET message = CONCAT(message,"Adding stars_in_movies relation (",new_sid,", ",new_mid,")");
        INSERT INTO stars_in_movies(starId,movieId) VALUES (new_sid,new_mid);
    END IF;
    

    IF (NOT EXISTS (SELECT 1 FROM genres WHERE name=Agenre_name LIMIT 1))
    THEN
        SET message = CONCAT(message,"The genre was not found. Inserting genre into table with id=",new_gid);
        INSERT INTO genres(id,name) VALUES (new_gid,Agenre_name);
        SET message = CONCAT(message,"Adding genres_in_movies relation (",new_gid,", ",new_mid,")");
   		INSERT INTO genres_in_movies(genreId,movieId) VALUES (new_gid,new_mid);
    ELSE
    		SELECT id INTO tempGenreId FROM genres WHERE name=Agenre_name;
    		IF (NOT EXISTS (SELECT * FROM genres_in_movies WHERE genre))
    		THEN
    			INSERT INTO genres_in_movies(genreId,movieId) VALUES (tempGenreId, new_mid);
    		END IF;
    END IF; 
    
    SET ID = 1;
END
$$

DELIMITER ;