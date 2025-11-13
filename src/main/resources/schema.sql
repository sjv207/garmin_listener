-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema garmin_connect
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `garmin_connect` ;

-- -----------------------------------------------------
-- Schema garmin_connect
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `garmin_connect` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `garmin_connect` ;

-- -----------------------------------------------------
-- Table `garmin_connect`.`admin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `garmin_connect`.`admin` ;

CREATE TABLE IF NOT EXISTS `garmin_connect`.`admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` TEXT NOT NULL,
  `password` TEXT NOT NULL,
  `last_login` TIMESTAMP NULL DEFAULT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `garmin_connect`.`garmin_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `garmin_connect`.`garmin_user` ;

CREATE TABLE IF NOT EXISTS `garmin_connect`.`garmin_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `garmin_user_id` VARCHAR(50) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_garmin_user_id` (`garmin_user_id` ASC) VISIBLE,
  UNIQUE INDEX `garmin_user_id_UNIQUE` (`garmin_user_id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `garmin_connect`.`user_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `garmin_connect`.`user_token` ;

CREATE TABLE IF NOT EXISTS `garmin_connect`.`user_token` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `access_token` VARCHAR(1024) NULL DEFAULT NULL,
  `access_token_expires` INT NULL DEFAULT NULL,
  `token_type` VARCHAR(50) NULL DEFAULT NULL,
  `refresh_token` VARCHAR(200) NULL DEFAULT NULL,
  `refresh_token_expires` INT NULL DEFAULT NULL,
  `scope` VARCHAR(250) NOT NULL,
  `jti` VARCHAR(100) NULL DEFAULT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_access_token` (`access_token`(255) ASC) VISIBLE,
  CONSTRAINT `user_token_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `garmin_connect`.`garmin_user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `garmin_connect`.`user_activity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `garmin_connect`.`user_activity` ;

CREATE TABLE IF NOT EXISTS `garmin_connect`.`user_activity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `garmin_user_id` INT NOT NULL,
  `activity_id` VARCHAR(20) NOT NULL,
  `activity_type` VARCHAR(45) NOT NULL,
  `start_time` BIGINT(12) NOT NULL,
  `duration_in_seconds` INT NOT NULL,
  `json_data` LONGTEXT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `activityId_UNIQUE` (`activity_id` ASC) VISIBLE,
  INDEX `fk_UserActivith_garmin_user1_idx` (`garmin_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_UserActivith_garmin_user1`
    FOREIGN KEY (`garmin_user_id`)
    REFERENCES `garmin_connect`.`garmin_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garmin_connect`.`user_health`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `garmin_connect`.`user_health` ;

CREATE TABLE IF NOT EXISTS `garmin_connect`.`user_health` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `garmin_user_id` INT NOT NULL,
  `health_type` VARCHAR(45) NOT NULL,
  `calendar_date` DATE NULL,
  `start_time` BIGINT(12) NULL,
  `json_data` LONGTEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_UserHealth_garmin_user1_idx` (`garmin_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_UserHealth_garmin_user1`
    FOREIGN KEY (`garmin_user_id`)
    REFERENCES `garmin_connect`.`garmin_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
