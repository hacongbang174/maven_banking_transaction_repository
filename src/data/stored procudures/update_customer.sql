CREATE DEFINER=`root`@`localhost` PROCEDURE `update_customer`(
	IN id_current BIGINT,
	IN name_update varchar(255),
	IN email_update varchar(50),
	IN phone_update varchar(255),
	IN address_update varchar(255),
    OUT success BOOLEAN
    )
BEGIN

    START TRANSACTION;

    UPDATE customers 
    SET 
        `name` = name_update,
        email = email_update,
        phone = phone_update,
        address = address_update
    WHERE id = id_current;

    IF ROW_COUNT() > 0 THEN
        SET success = TRUE;
	ELSE
		SET success = FALSE;
    END IF;

    IF success THEN
        COMMIT;
    ELSE
        ROLLBACK;
    END IF;

    SELECT success;
END