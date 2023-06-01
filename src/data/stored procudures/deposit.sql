CREATE DEFINER=`root`@`localhost` PROCEDURE `deposit`(
    IN id_customer BIGINT, 
    IN amount DECIMAL(12,0)
)
BEGIN
	DECLARE success BOOLEAN;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION, SQLWARNING
    BEGIN
        ROLLBACK;
        SET success = FALSE;
    END;

    START TRANSACTION;

    UPDATE `customers` SET balance = balance + amount WHERE id = id_customer;
    IF ROW_COUNT() > 0 THEN
		INSERT INTO `deposits` (createdAt, transactionAmount, customer_id)
        VALUES (CURDATE(), amount, id_customer);
		IF ROW_COUNT() > 0 THEN
			SET success = TRUE;
		ELSE
			SET success = FALSE;
		END IF;
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