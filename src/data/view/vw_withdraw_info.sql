CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `vw_withdraw_info` AS
    SELECT 
        `w`.`id` AS `id`,
        `c`.`name` AS `withdrawerName`,
        `w`.`transactionAmount` AS `transactionAmount`,
        `w`.`createdAt` AS `createdAt`
    FROM
        (`withdraws` `w`
        JOIN `customers` `c` ON ((`c`.`id` = `w`.`customer_id`)))