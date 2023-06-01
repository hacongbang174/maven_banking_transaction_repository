CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `vw_deposit_info` AS
    SELECT 
        `d`.`id` AS `id`,
        `c`.`name` AS `depositerName`,
        `d`.`transactionAmount` AS `transactionAmount`,
        `d`.`createdAt` AS `createdAt`
    FROM
        (`deposits` `d`
        JOIN `customers` `c` ON ((`c`.`id` = `d`.`customer_id`)))