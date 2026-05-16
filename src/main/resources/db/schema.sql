-- =========================================================
-- Pintrip DB Schema (Session + Dong + Place + Quest)
-- 요구사항 반영:
-- 1) 로그인 없음, 세션 기반
-- 2) 최종 동 선택 시점에 세션 생성
-- 3) 동 10개, 동별 place 3개
-- 4) place는 이미지 1장 보유 (업로드 위치 미정)
-- 5) quest 20개, place FK 연결
-- =========================================================

DROP TABLE IF EXISTS trip_sessions;
DROP TABLE IF EXISTS quests;
DROP TABLE IF EXISTS places;
DROP TABLE IF EXISTS dongs;

CREATE TABLE dongs
(
    id         BIGINT PRIMARY KEY,                             -- 동 식별자(PK). 목데이터 고정 ID 사용
    name       VARCHAR(100) NOT NULL UNIQUE,                   -- 동 이름(예: 성수1동). 프론트가 최종 1개 선택
--     city VARCHAR(50) NOT NULL,                      -- 시/도 정보(예: 서울특별시)
--     district VARCHAR(50) NOT NULL,                  -- 구 정보(예: 성동구)
--     description VARCHAR(255),                       -- 동에 대한 짧은 설명(선택)
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,             -- 노출 여부(목데이터 비활성화 용도)
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP -- 생성 시각
);

-- CREATE TABLE places (
--     id BIGINT PRIMARY KEY,                           -- place 식별자(PK)
--     dong_id BIGINT NOT NULL,                         -- 소속 동 ID(FK -> dongs.id)
--     name VARCHAR(120) NOT NULL,                      -- 장소명
--     category VARCHAR(50) NOT NULL,                   -- 장소 카테고리(골목/카페/공원 등)
--     description VARCHAR(255),                        -- 장소 설명(프론트 카드 표기용)
--     image_url VARCHAR(500) NOT NULL,                 -- 장소 대표 이미지 경로/URL (스토리지 미정이라 mock URL 사용)
--     map_keyword VARCHAR(150) NOT NULL,               -- 지도 검색용 키워드(네이버/카카오 링크 생성용)
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 생성 시각
--     CONSTRAINT fk_places_dong
--         FOREIGN KEY (dong_id) REFERENCES dongs(id)
-- );
--
-- CREATE TABLE quests (
--     id BIGINT PRIMARY KEY,                           -- quest 식별자(PK)
--     place_id BIGINT NOT NULL,                        -- 연결된 place ID(FK -> places.id)
--     title VARCHAR(120) NOT NULL,                     -- 퀘스트 제목
--     description VARCHAR(300) NOT NULL,               -- 퀘스트 상세 설명(수행 가이드)
--     difficulty VARCHAR(20) NOT NULL DEFAULT 'EASY',  -- 난이도(EASY/MEDIUM/HARD)
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 생성 시각
--     CONSTRAINT fk_quests_place
--         FOREIGN KEY (place_id) REFERENCES places(id)
-- );

CREATE TABLE trip_sessions
(
    id         VARCHAR(36) PRIMARY KEY,                        -- 세션 ID(UUID 문자열). 로그인 대체 식별자
    dong_id    BIGINT      NOT NULL,                           -- 사용자가 최종 선택한 동 ID(FK -> dongs.id), 세션 생성 시 필수
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',          -- 세션 상태(ACTIVE/COMPLETED/EXPIRED)
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 세션 생성 시각
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 마지막 수정 시각
    expired_at TIMESTAMP   NOT NULL                            -- 세션 만료 시각(생성 후 2일)
--     CONSTRAINT fk_sessions_dong
--         FOREIGN KEY (dong_id) REFERENCES dongs(id),
--     CONSTRAINT fk_sessions_place
--         FOREIGN KEY (selected_place_id) REFERENCES places(id),
--     CONSTRAINT fk_sessions_quest
--         FOREIGN KEY (selected_quest_id) REFERENCES quests(id)
);

CREATE TABLE trip_quests
(
    id                VARCHAR(36) PRIMARY KEY,
    session_id        VARCHAR(36) NOT NULL, -- 소속된 세션 ID
    place_description VARCHAR(100),         -- 현재 세션에서 선택된 place (텍스트 그대로)
    quest_description VARCHAR(100),         -- 현재 세션에서 선택된 quest (텍스트 그대로)
    discovery         varchar(100),         -- 발견한 것
    review            VARCHAR(300),         -- 짧은 후기
    is_completed      BOOLEAN               -- 완료 여부
)

-- CREATE INDEX idx_places_dong_id ON places(dong_id);          -- 동별 place 조회 최적화
-- CREATE INDEX idx_quests_place_id ON quests(place_id);        -- place별 quest 조회 최적화
-- CREATE INDEX idx_sessions_dong_id ON trip_sessions(dong_id); -- 동 기준 세션 조회 최적화
