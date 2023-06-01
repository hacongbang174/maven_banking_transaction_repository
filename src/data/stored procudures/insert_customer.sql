CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_customer`(
	IN name_create varchar(255),
	IN email_create varchar(50),
	IN phone_create varchar(255),
	IN address_create varchar(255),
	OUT success BOOLEAN
)
BEGIN

    START TRANSACTION;
    
    INSERT INTO customers (`name`, email, phone, address)
    VALUES (name_create, email_create, phone_create, address_create);
    
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