DELIMITER $$
DROP PROCEDURE IF EXISTS add_star;
CREATE PROCEDURE add_star(IN sname varchar(100),IN syear INT,OUT ID varchar(10))
BEGIN
    
    DECLARE sCHARS varchar(2);
    DECLARE sINT INT(7) ZEROFILL;
    DECLARE old_sid varchar(10);
    DECLARE sid varchar(10);
    DECLARE star_number varchar(10);

    SELECT LEFT(star_id,2), RIGHT(star_id, 7), star_id
           INTO sCHARS, sINT, old_sid
    FROM maximum WHERE star_id = star_id;
    
    SET sINT = sINT+1;
    SET sid = CONCAT(sCHARS,sINT);
 
    SELECT id INTO star_number FROM stars WHERE id = id AND name = sname LIMIT 1;
    IF star_number IS NULL
    THEN
        SELECT CONCAT("The star was not found. Inserting star into table with id=",sid);
        INSERT INTO stars(id,name,birthYear) VALUES (sid,sname, syear);
        UPDATE maximum
            SET star_id = sid
            WHERE star_id = old_sid;
        SET ID = sid;
    ELSE 
        SET ID = star_number;
    END IF;
    /*
    SELECT CONCAT("Adding stars_in_movies relation (",sid,", ",new_mid,")");
    INSERT INTO stars_in_movies(starId,movieId) VALUES (new_sid,new_mid);
    */
END
$$

DELIMITER ;
