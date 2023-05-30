CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `vw_transfer_info` AS
    SELECT 
        `t`.`id` AS `transferId`,
        `c_sender`.`id` AS `senderId`,
        `c_sender`.`name` AS `senderName`,
        `c_recipient`.`id` AS `recipientId`,
        `c_recipient`.`name` AS `recipientName`,
        `t`.`transferAmount` AS `transferAmount`,
        `t`.`transactionAmount` AS `transactionAmount`,
        `t`.`fees` AS `fees`,
        `t`.`feesAmount` AS `feesAmount`
    FROM
        ((`customers` `c_sender`
        JOIN `transfers` `t` ON ((`c_sender`.`id` = `t`.`sender_id`)))
        JOIN `customers` `c_recipient` ON ((`c_recipient`.`id` = `t`.`recipient_id`)))