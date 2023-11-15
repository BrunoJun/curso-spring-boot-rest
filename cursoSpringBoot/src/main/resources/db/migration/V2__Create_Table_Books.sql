-- -----------------------------------------------------
-- Table `curso_spring_boot`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `books` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `author` VARCHAR(100) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`));
