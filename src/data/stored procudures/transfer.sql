CREATE DEFINER=`root`@`localhost` PROCEDURE `transfer`(IN sender_id BIGINT, IN recipient_id BIGINT, IN amount DECIMAL(65,0))
BEGIN
	DECLARE success BOOLEAN DEFAULT TRUE;
	DECLARE fees DECIMAL(12,2);
    DECLARE fees_amount DECIMAL(12,0);
    DECLARE transaction_amount DECIMAL(12,0);
	START TRANSACTION;
	IF recipient_id > 0 AND sender_id > 0 THEN
		IF (SELECT checkIdAvailable(sender_id)) THEN
			IF (SELECT checkIdAvailable(recipient_id)) THEN
				IF (SELECT validateAmount(amount)) THEN
					SET fees = 0.10;
					SET fees_amount = fees * amount;
					SET transaction_amount = amount + fees_amount;
                    SET @balance_sender = (SELECT balance FROM customers WHERE id = sender_id) - transaction_amount;
                    SET @balance_recipient = (SELECT balance FROM customers WHERE id = recipient_id) + amount;
                    IF (SELECT validateAmount(@balance_recipient)) THEN
						IF transaction_amount < (SELECT balance FROM customers WHERE id = sender_id) THEN
							UPDATE customers SET balance =  @balance_sender WHERE id = sender_id;
							UPDATE customers SET balance = @balance_recipient WHERE id = recipient_id;
							insert into transfers (createdAt, fees, feesAmount, transactionAmount, transferAmount, recipient_id, sender_id)
							values (CURDATE(),0.10, fees_amount, amount + fees_amount , amount, recipient_id, sender_id);
							COMMIT;
							SET success = TRUE;
						ELSE
							ROLLBACK;
							SET success = FALSE;
						END IF;
                    ELSE
						ROLLBACK;
						SET success = FALSE;
                    END IF;
				ELSE
					ROLLBACK;
					SET success = FALSE;
				END IF;
            ELSE
				ROLLBACK;
				SET success = FALSE;
            END IF;
		ELSE
			ROLLBACK;
				SET success = FALSE;
		END IF;
    ELSE
		ROLLBACK;
		SET success = FALSE;
    END IF;
    SELECT success;
END