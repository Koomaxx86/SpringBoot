-- Active: 1701683907642@@127.0.0.1@3306@joeun
-- 기존 테이블 존재하면 삭제
DROP TABLE IF EXISTS user;


-- user : 회원 테이블
CREATE TABLE `user` (
  `NO` int NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(100) NOT NULL,
  `USER_PW` varchar(200) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `EMAIL` varchar(200) DEFAULT NULL,
  `REG_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPD_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ENABLED` int DEFAULT 1,
  PRIMARY KEY (`NO`)
) COMMENT='회원';

-- BCryptPasswordEncoder - 암호화 시
-- 사용자
INSERT INTO user ( user_id, user_pw, name, email )
VALUES ( 'user', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '사용자', 'user@mail.com' );

-- 관리자
INSERT INTO user ( user_id, user_pw, name, email )
VALUES ( 'admin', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '관리자', 'admin@mail.com' );

