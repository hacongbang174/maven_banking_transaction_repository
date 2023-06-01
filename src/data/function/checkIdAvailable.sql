CREATE DEFINER=`root`@`localhost` FUNCTION `checkIdAvailable`(id_customer BIGINT) RETURNS tinyint(1)
    DETERMINISTIC
BEGIN
	DECLARE count INT;
    SET count = (SELECT count(id) FROM customers WHERE id = id_customer);
    IF count <> 0 THEN
		RETURN TRUE;
	ELSE
		RETURN FALSE;
	END IF;
END