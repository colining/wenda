DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `date_index` (`created_date` ASC));

  DROP TABLE IF EXISTS `user`;
  CREATE TABLE `user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL DEFAULT '',
    `password` varchar(128) NOT NULL DEFAULT '',
    `salt` varchar(32) NOT NULL DEFAULT '',
    `head_url` varchar(256) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME NULL,
  `user_id` INT NULL,
  `data` TINYTEXT NULL,
  `type` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_index` (`user_id` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;
# 用于测试时初始化数据库，这样就会让数据库一直保持一个样子，
# 要不然每次测试数据库都不一样那不疯了