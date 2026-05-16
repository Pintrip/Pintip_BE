# DB 구조 가이드 (CSV 매핑 반영)

소스 CSV: `pintrip_image_quest_mapping_with_neighborhood.csv`  
핵심 모델: **동네 1개당 이미지 카드 3개**, 카드마다 **퀘스트 3개(문구+설명)**를 연결한다.

---

## 테이블 요약

### `dongs`
- 동네 마스터(10개)
- `id`, `name`, `is_active`

### `dong_image_mappings`
- 동네별 이미지 카드(30개)
- `image_file`, `image_headline`, `image_sub_description`

### `image_card_quests`
- 카드별 퀘스트(90개, 카드당 3개)
- `image_card_id`, `quest_order`, `quest_title`, `quest_description`

### `trip_sessions`
- 기존 UUID 세션 방식 유지 (로그인 없음)
- `dong_id`, `status`, `created_at`, `updated_at`, `expired_at` (만료: 생성 후 **2일**)

### `trip_session_quest_reviews`
- 세션별 퀘스트 후기 (퀘스트당 1개)
- `session_id`, `image_card_id`, `quest_id`, `discovered_note`, `review_text`, `mood_tags`(JSON)
- `UNIQUE(session_id, quest_id)`

---

## API 매핑

상세: [../../docs/핀트립_ERD.md](../../docs/핀트립_ERD.md) §3, [../../docs/백엔드_개발_실행계획.md](../../docs/백엔드_개발_실행계획.md)

---

## 초기화 파일

- `schema.sql`: 테이블 정의
- `data.sql`: CSV 기반 seed 데이터
- `rds-migrate.sql`: RDS에 JPA로 만든 테이블에 `created_at` DEFAULT 맞추기 (1회)

### RDS에 seed 넣기 (순서 고정)

```text
1. dongs INSERT (created_at 포함) — 이미 10건 있으면 스킵
2. rds-reset-content.sql  ← quest_code_1 오류 시 필수 (구 mappings DROP)
3. data.sql 에서 dong_image_mappings + image_card_quests 만 실행
```

| 증상 | 조치 |
|------|------|
| `quest_code_1 doesn't have a default value` | **구 테이블** → `rds-reset-content.sql` 실행 후 mappings INSERT |
| `Duplicate entry '1' for key 'dongs.PRIMARY'` | dongs는 **이미 들어감** → 다시 INSERT 하지 말 것 |
| `image_card_quests` 없음 | `rds-reset-content.sql` 후 quests INSERT |

---

로컬 MySQL 볼륨을 초기화해야 새 seed를 다시 적용할 수 있다.

```bash
docker compose down -v
docker compose up -d
./gradlew bootRun
```
