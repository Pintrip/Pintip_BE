-- =========================================================
-- Pintrip DB Schema (CSV: 동네-이미지-퀘스트 매핑 기준)
-- 로그인/인증 없음, 세션 UUID 기반 유지
-- =========================================================

DROP TABLE IF EXISTS trip_sessions;
DROP TABLE IF EXISTS dong_image_mappings;
DROP TABLE IF EXISTS quest_templates;
DROP TABLE IF EXISTS dongs;

-- 동네 마스터 (프론트 동 선택·랜덤 UI용, 10개)
CREATE TABLE dongs
(
    id         BIGINT PRIMARY KEY,                             -- 동네 PK (seed 고정 1~10)
    name       VARCHAR(100) NOT NULL UNIQUE,                   -- 동네 표시명 (예: 성수동, 망원동)
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,             -- 목록 노출 여부 (false면 API에서 제외)
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP -- 레코드 생성 시각
);

-- 퀘스트 템플릿 마스터 (Q01~Q18, 여러 동네 카드에서 재사용)
CREATE TABLE quest_templates
(
    code        VARCHAR(10) PRIMARY KEY,                        -- 퀘스트 코드 (Q01, Q02, … Q18)
    title       VARCHAR(200)  NOT NULL,                         -- 퀘스트 제목 (예: 낡은 간판 찾기)
    description VARCHAR(1000) NOT NULL,                         -- 수행 가이드 문구 (프론트 카드 본문)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 동네별 이미지 카드 + 퀘스트 3개 묶음 (동네당 3장, 총 30장)
-- 실제 이미지 파일은 프론트 정적 자산, DB에는 파일명만 저장
CREATE TABLE dong_image_mappings
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 카드 PK (고정 카드 목록 API 응답 식별자)
    dong_id               BIGINT       NOT NULL,               -- 소속 동네 FK → dongs.id
    image_file            VARCHAR(255) NOT NULL,               -- 이미지 파일명 (프론트: /images/.../{image_file})
    image_headline        VARCHAR(255) NOT NULL,               -- 카드 대표 문장 (이미지 대표문장)
    image_sub_description VARCHAR(500) NOT NULL,               -- 카드 보조 설명
    quest_code_1          VARCHAR(10)  NOT NULL,               -- 연결 퀘스트 1 → quest_templates.code
    quest_code_2          VARCHAR(10)  NOT NULL,               -- 연결 퀘스트 2
    quest_code_3          VARCHAR(10)  NOT NULL,               -- 연결 퀘스트 3
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mapping_dong FOREIGN KEY (dong_id) REFERENCES dongs (id),
    CONSTRAINT fk_mapping_quest_1 FOREIGN KEY (quest_code_1) REFERENCES quest_templates (code),
    CONSTRAINT fk_mapping_quest_2 FOREIGN KEY (quest_code_2) REFERENCES quest_templates (code),
    CONSTRAINT fk_mapping_quest_3 FOREIGN KEY (quest_code_3) REFERENCES quest_templates (code)
);

-- 여행 세션 (로그인 대체: UUID). 프론트는 sessionId를 localStorage 등에 보관
CREATE TABLE trip_sessions
(
    id                  VARCHAR(36) PRIMARY KEY,               -- 세션 UUID (X-Session-Id 헤더·URL path)
    dong_id             BIGINT      NOT NULL,                  -- 사용자가 확정한 동네 FK → dongs.id
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE | COMPLETED | EXPIRED
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 상태 변경 시 갱신
    expired_at          TIMESTAMP   NOT NULL,                  -- 세션 만료 시각 (생성 후 약 2일)
    CONSTRAINT fk_sessions_dong FOREIGN KEY (dong_id) REFERENCES dongs (id)
);

CREATE INDEX idx_mapping_dong_id ON dong_image_mappings (dong_id);
CREATE INDEX idx_sessions_dong_id ON trip_sessions (dong_id);
