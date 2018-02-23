DELIMITER $$
DROP PROCEDURE IF EXISTS add_star;
CREATE PROCEDURE add_star(IN sname varchar(100),IN syear INT,OUT ID INT)
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
 
    IF (SELECT EXISTS (SELECT 1 FROM stars WHERE name=sname LIMIT 1)) 
    THEN
    		UPDATE stars 
    		SET birthYear = syear 
    		WHERE name=sname;
    		SET ID = 1;
    ELSE
        SELECT CONCAT("The star was not found. Inserting star into table with id=",sid);
        INSERT INTO stars(id,name,birthYear) VALUES (sid,sname, syear);
        SET ID = ROW_COUNT();
        UPDATE maximum
            SET star_id = sid
            WHERE star_id = old_sid;
    END IF;
END
$$

DELIMITER ;
