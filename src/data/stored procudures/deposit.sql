CREATE DEFINER=`root`@`localhost` PROCEDURE `deposit`(IN id_customer BIGINT, IN amount DECIMAL(65,0))
BEGIN
IF id_customer > 0 THEN
		IF (SELECT checkIdAvailable(id_customer)) THEN
			IF (SELECT validateAmount(amount)) THEN
				SET @balance = (SELECT balance FROM customers WHERE id = id_customer) + amount;
				IF (SELECT validateAmount(@balance)) THEN
					UPDATE `customers` SET balance = @balance WHERE id = id_customer;
					COMMIT;
					SELECT 'Giao dịch thành công.' AS `message`;
				ELSE
					ROLLBACK;
					SELECT 'Tổng tiền gửi vượt quá định mức. Tổng tiền gửi nhỏ hơn 12 chữ số.' AS `message`;
                END IF;
			ELSE
				ROLLBACK;
				SELECT 'Số tiền gửi không hợp lệ. Phải lớn hơn 0 và nhỏ hơn 12 chữ số.' AS `message`;
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