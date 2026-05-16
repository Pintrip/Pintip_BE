-- rds-reset-content.sql 과 동일 (호환용 파일명)
-- 구 dong_image_mappings(quest_code_1…) DROP 후 신 스키마 생성

USE `pintrip-db`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS trip_session_quest_reviews;
DROP TABLE IF EXISTS image_card_quests;
DROP TABLE IF EXISTS quests;
DROP TABLE IF EXISTS places;
DROP TABLE IF EXISTS quest_templates;
DROP TABLE IF EXISTS dong_image_mappings;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE dong_image_mappings
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    dong_id               BIGINT       NOT NULL,
    image_file            VARCHAR(255) NOT NULL,
    image_headline        VARCHAR(255) NOT NULL,
    image_sub_description VARCHAR(500) NOT NULL,
    CONSTRAINT fk_mapping_dong FOREIGN KEY (dong_id) REFERENCES dongs (id)
);

CREATE TABLE image_card_quests
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_card_id     BIGINT        NOT NULL,
    quest_order       INT           NOT NULL,
    quest_title       VARCHAR(200)  NOT NULL,
    quest_description VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_card_quest_mapping FOREIGN KEY (image_card_id) REFERENCES dong_image_mappings (id),
    CONSTRAINT uq_card_quest_order UNIQUE (image_card_id, quest_order)
);

CREATE INDEX idx_mapping_dong_id ON dong_image_mappings (dong_id);
CREATE INDEX idx_card_quest_card_id ON image_card_quests (image_card_id);

CREATE TABLE trip_session_quest_reviews
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id      VARCHAR(36)   NOT NULL,
    image_card_id   BIGINT        NOT NULL,
    quest_id        BIGINT        NOT NULL,
    discovered_note VARCHAR(500)  NOT NULL,
    review_text     VARCHAR(2000) NOT NULL,
    mood_tags       JSON          NULL,
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_session FOREIGN KEY (session_id) REFERENCES trip_sessions (id),
    CONSTRAINT fk_review_image_card FOREIGN KEY (image_card_id) REFERENCES dong_image_mappings (id),
    CONSTRAINT fk_review_quest FOREIGN KEY (quest_id) REFERENCES image_card_quests (id),
    CONSTRAINT uq_session_quest_review UNIQUE (session_id, quest_id)
);

CREATE INDEX idx_review_session_id ON trip_session_quest_reviews (session_id);
