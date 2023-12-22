-- Active: 1701683907642@@127.0.0.1@3306@joeun
-- 🔄 persistent_logins  --


-- 기존 테이블 존재하면 삭제
DROP TABLE IF EXISTS persistent_logins;


-- persistent_logins : 자동 로그인 테이블
create table persistent_logins (
    username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);
