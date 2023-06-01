CREATE DEFINER=`root`@`localhost` FUNCTION `validateAmount`(amount DECIMAL(65,0)) RETURNS tinyint(1)
    DETERMINISTIC
BEGIN
    DECLARE is_valid BOOLEAN;
    SET is_valid = amount REGEXP '^[0-9]+$';
    IF is_valid THEN
        SET is_valid = LENGTH(amount) <= 12;
    END IF;
    IF is_valid THEN
        SET is_valid = amount REGEXP '^[0-9]+$';
    END IF;
    RETURN is_valid;
END