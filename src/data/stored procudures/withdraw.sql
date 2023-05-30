CREATE DEFINER=`root`@`localhost` PROCEDURE `withdraw`(IN id_customer INT, IN amount DECIMAL(65,0))
BEGIN
    SET @balance = (SELECT balance FROM customers WHERE id = id_customer);
    START TRANSACTION;
    IF id_customer > 0 THEN
		IF (SELECT checkIdAvailable(id_customer)) THEN
			IF (SELECT validateAmount(amount)) THEN
				IF amount < @balance THEN
					IF amount > 0 THEN
						UPDATE `customers` SET balance = balance - amount WHERE id = id_customer;
						COMMIT;
						SELECT 'Giao dịch thành công.' AS `message`;
					ELSE	
						ROLLBACK;
						SELECT 'Số tiền rút phải lớn hơn 0' AS `message`;
					END IF;
				ELSE	
					ROLLBACK;
					SELECT CONCAT('Không đủ số dư để rút tiền. Vui lòng rút số tiền nhỏ hơn ', @balance, ' VNĐ') AS `message`;
				END IF;
			ELSE
				ROLLBACK;
				SELECT 'Số tiền rút không hợp lệ. Phải lớn hơn 0 và nhỏ hơn 12 chữ số.' AS `message`;
			END IF;
		ELSE
			ROLLBACK;
			SELECT 'ID hiện tại không có.' AS `message`;
		END IF;
    ELSE
		ROLLBACK;
		SELECT 'ID không hợp lệ. ID phải là số nguyên và lớn hơn 0' AS `message`;
    END IF;
END