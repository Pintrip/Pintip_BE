-- RDS: 기존 JPA 테이블에 TIMESTAMP DEFAULT만 맞출 때 (테이블이 이미 있을 때)
-- image_card_quests 가 없으면 → rds-bootstrap.sql 먼저 실행

USE `pintrip-db`;

ALTER TABLE dongs
    MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- dong_image_mappings / image_card_quests 는 JPA에 created_at 없음 → ALTER 하지 않음

ALTER TABLE trip_sessions
    MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    MODIFY updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE trip_session_quest_reviews
    MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    MODIFY updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
