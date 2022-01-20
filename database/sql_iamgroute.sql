-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema groute
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema groute
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `groute` DEFAULT CHARACTER SET utf8 ;
USE `groute` ;

-- -----------------------------------------------------
-- Table `groute`.`route`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`route` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `day` INT NOT NULL COMMENT '일자별 경로\\nday1, day2...',
  `memo` VARCHAR(255) NULL DEFAULT NULL,
  `isCustom` VARCHAR(1) NOT NULL COMMENT '사용자가 커스텀한 route인지',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`user` (
  `id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(100) NULL DEFAULT NULL,
  `phone` VARCHAR(13) NULL DEFAULT NULL,
  `gender` VARCHAR(1) NULL DEFAULT NULL,
  `birth` DATE NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `type` VARCHAR(10) NOT NULL,
  `token` VARCHAR(255) NULL DEFAULT NULL,
  `img` VARCHAR(255) NULL DEFAULT NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`userplan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`userplan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(100) NOT NULL,
  `title` VARCHAR(100) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `startDate` DATE NOT NULL,
  `endDate` DATE NOT NULL,
  `totalDate` VARCHAR(20) NOT NULL,
  `isPublic` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_userPlan_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_userPlan`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`routes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`routes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `route_id` INT NOT NULL,
  `plan_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_idx` (`plan_id` ASC) VISIBLE,
  INDEX `fk_idx1` (`route_id` ASC) VISIBLE,
  CONSTRAINT `fk_route_routes`
    FOREIGN KEY (`route_id`)
    REFERENCES `groute`.`route` (`id`),
  CONSTRAINT `fk_user_routes`
    FOREIGN KEY (`plan_id`)
    REFERENCES `groute`.`userplan` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `spentMoney` INT NOT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL COMMENT '결제 내역',
  `category` VARCHAR(10) NULL DEFAULT NULL COMMENT '숙소/항공/교통/관광/식비/쇼핑/기타',
  `routes_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_account_routes1_idx` (`routes_id` ASC) VISIBLE,
  CONSTRAINT `fk_account_routes1`
    FOREIGN KEY (`routes_id`)
    REFERENCES `groute`.`routes` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`area`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`area` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `img` VARCHAR(255) NULL DEFAULT NULL,
  `lat` VARCHAR(20) NOT NULL,
  `lng` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`theme`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`place`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`place` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(100) NOT NULL,
  `lat` VARCHAR(20) NOT NULL,
  `lng` VARCHAR(20) NOT NULL,
  `zipCode` VARCHAR(7) NOT NULL,
  `contact` VARCHAR(100) NULL DEFAULT NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `theme_id` INT NOT NULL,
  `area_id` INT NOT NULL,
  `img` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` VARCHAR(100) NOT NULL COMMENT 'user_id에는 admin 또는 place를 등록한 user의 아이디\\n동진님 - plan이 public이 될 때, 검토 후에  userplace가 place로 등록이 될 수 있도록(user_id -> admin)\\n',
  `rate` DOUBLE NULL,
  `heartCnt` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_theme_place_idx` (`theme_id` ASC) VISIBLE,
  INDEX `fk_area_place_idx` (`area_id` ASC) VISIBLE,
  INDEX `fk_user_place_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_area_place`
    FOREIGN KEY (`area_id`)
    REFERENCES `groute`.`area` (`id`),
  CONSTRAINT `fk_theme_place`
    FOREIGN KEY (`theme_id`)
    REFERENCES `groute`.`theme` (`id`),
  CONSTRAINT `fk_user_place`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 282
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`planshareuser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`planshareuser` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(100) NOT NULL,
  `plan_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_planShareUser_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_userPlan_planShareUser_idx` (`plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_planShareUser`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`),
  CONSTRAINT `fk_userPlan_planShareUser`
    FOREIGN KEY (`plan_id`)
    REFERENCES `groute`.`userplan` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`routedetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`routedetail` (
  `route_id` INT NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  `place_id` INT NOT NULL,
  `priority` INT NOT NULL COMMENT '우선순위에 따라 장소 정렬',
  `memo` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_route_routeDetail_idx` (`route_id` ASC) VISIBLE,
  INDEX `fk_place_routeDetail_idx` (`place_id` ASC) VISIBLE,
  CONSTRAINT `fk_place_routeDetail`
    FOREIGN KEY (`place_id`)
    REFERENCES `groute`.`place` (`id`),
  CONSTRAINT `fk_route_routeDetail`
    FOREIGN KEY (`route_id`)
    REFERENCES `groute`.`route` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `groute`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`board` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`boardDetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`boardDetail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `content` TEXT NOT NULL,
  `img` VARCHAR(255) NULL,
  `create_date` TIMESTAMP NULL,
  `update_date` TIMESTAMP NULL,
  `hearCnt` INT NULL,
  `hitCnt` INT NULL,
  `board_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  `place_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_boardDetail_board1_idx` (`board_id` ASC) VISIBLE,
  INDEX `fk_boardDetail_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_boardDetail_place1_idx` (`place_id` ASC) VISIBLE,
  CONSTRAINT `fk_boardDetail_board1`
    FOREIGN KEY (`board_id`)
    REFERENCES `groute`.`board` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_boardDetail_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_boardDetail_place1`
    FOREIGN KEY (`place_id`)
    REFERENCES `groute`.`place` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `groute`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `groute`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(255) NOT NULL,
  `level` INT NOT NULL,
  `groupNum` INT NOT NULL,
  `order` INT NOT NULL,
  `boardDetail_id` INT NOT NULL,
  `user_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_comment_boardDetail1_idx` (`boardDetail_id` ASC) VISIBLE,
  INDEX `fk_comment_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_boardDetail1`
    FOREIGN KEY (`boardDetail_id`)
    REFERENCES `groute`.`boardDetail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `groute`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
