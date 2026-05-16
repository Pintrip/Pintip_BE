# 핀트립 프론트 API 연동 가이드

백엔드: AWS Elastic Beanstalk (`ap-northeast-2`)  
프론트: Vercel — **https://pintrip-fe.vercel.app**

API·플로우 상세: [백엔드_개발_실행계획.md](백엔드_개발_실행계획.md)

---

## 0. 화면 플로우 (권장)

```text
1. GET /dongs → 프론트에서 동네 랜덤 UI
2. GET /dongs/{dongId}/image-cards → 카드·퀘스트 랜덤 UI
3. POST /trip-sessions { dongId } → sessionId 저장
4. 퀘스트 수행 중 PUT .../quest-reviews/{questId} → 3건 시 status 자동 COMPLETED
5. PATCH .../complete (선택, 수동 완료)
6. 새로고침 시 GET /trip-sessions/{sessionId}
   + GET /dongs/{dongId}/image-cards?sessionId= (quests[].isCompleted)
   + GET .../quest-reviews
```

---

## 1. 해야 할 일 (체크리스트)

- [ ] 프로젝트 루트에 `vercel.json` 추가 (아래 그대로 복사)
- [ ] Vercel 환경 변수 `VITE_API_BASE_URL=/backend` 설정
- [ ] API 호출 시 **`/api` prefix 붙이지 않기** (`/dongs`, `/trip-sessions` …)
- [ ] 배포 후 `https://pintrip-fe.vercel.app/backend/health` 로 연동 확인

---

## 2. `vercel.json` (프로젝트 루트에 생성)

HTTPS(Vercel) → HTTP(EB) Mixed Content 방지용 **필수**입니다.

```json
{
  "rewrites": [
    {
      "source": "/backend/:path*",
      "destination": "http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com/:path*"
    }
  ]
}
```

> `destination`에는 `http://` + 호스트만 넣고, 끝에 `/` 없음.

---

## 3. 환경 변수

### Vercel (Production / Preview)

| 이름 | 값 |
|------|-----|
| `VITE_API_BASE_URL` | `/backend` |

Vercel 대시보드 → Project → Settings → Environment Variables

### 로컬 개발 (`.env.local` 예시)

```env
VITE_API_BASE_URL=http://localhost:8080
```

로컬 백엔드 포트가 다르면 맞게 수정 (예: 5000이면 `http://localhost:5000`).

---

## 4. API 클라이언트 예시

```ts
const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "/backend";

// 동 목록
const res = await fetch(`${API_BASE}/dongs`, {
  headers: { Accept: "application/json" },
});

// 세션 생성
await fetch(`${API_BASE}/trip-sessions`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ dongId: 1 }),
});
```

`sessionId`는 응답값을 `localStorage` 등에 저장 후 이후 URL에 사용.

---

## 5. API 목록 (P0)

**Base path 없음** — `/api/dongs` ❌ → `/dongs` ✅

| 메서드 | 경로 | Body / 비고 |
|--------|------|-------------|
| `GET` | `/dongs` | 동 10개 목록 |
| `GET` | `/dongs/{dongId}/image-cards` | 카드 3개 + 퀘스트 3개 (`?sessionId=` → `isCompleted`) |
| `POST` | `/trip-sessions` | `{ "dongId": number }` → `sessionId` |
| `GET` | `/trip-sessions/{sessionId}` | 세션 조회·복구 |
| `PUT` | `/trip-sessions/{sessionId}/quest-reviews/{questId}` | 퀘스트 후기 저장/수정 (아래 참고) |
| `GET` | `/trip-sessions/{sessionId}/quest-reviews` | 작성된 퀘스트 후기 목록 |
| `PATCH` | `/trip-sessions/{sessionId}/complete` | 세션 완료 |

**세션 완료(COMPLETED):** 후기 **3건** 저장 시 자동 · 생성 후 **2일** 경과 시 조회 시 자동 · `PATCH .../complete` 수동

**퀘스트 후기 PUT body 예시:**

```json
{
  "imageCardId": 1,
  "discoveredNote": "오래된 간판과 조용한 골목을 발견했다",
  "reviewText": "유명한 장소는 아니지만, 평범한 동네를 새롭게 보게 됐다",
  "moodTags": ["조용했다", "다시 가고 싶다"]
}
```

- `questId`: `GET /dongs/{dongId}/image-cards` 응답의 `quests[].questId`

공통: `Content-Type: application/json` (POST)

---

## 6. 연동 확인 URL

| 용도 | URL (브라우저 또는 curl) |
|------|---------------------------|
| 백엔드 직접 헬스 | http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com/health |
| **Vercel 경유 헬스** (rewrite 확인) | https://pintrip-fe.vercel.app/backend/health |
| 동 목록 | https://pintrip-fe.vercel.app/backend/dongs |

헬스 기대 응답: `{"status":"UP"}`

---

## 7. 트러블슈팅

| 증상 | 확인 |
|------|------|
| CORS **403** | EB `CORS_ALLOWED_ORIGINS`에 실제 Vercel URL + `https://*.vercel.app`(프리뷰) 포함. 또는 `VITE_API_BASE_URL=/backend`로 same-origin 프록시 사용 |
| Mixed Content / `ERR_BLOCKED` | `vercel.json` 적용·재배포, `VITE_API_BASE_URL=/backend` |
| 404 on `/api/...` | `/api` prefix 제거 (`/backend/dongs` 형태로 호출) |
| 502 | 백엔드 장애 — `/backend/health` 먼저 확인 |

---

## 8. 슬랙 복붙용 (짧은 버전)

```text
[핀트립 BE 연동]

프론트: https://pintrip-fe.vercel.app
백엔드 EB: http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com

1) vercel.json rewrites: /backend/* → 위 EB 호스트 (가이드 JSON 복사)
2) VITE_API_BASE_URL=/backend
3) API는 /api 없이 /dongs, /trip-sessions, …
4) 확인: https://pintrip-fe.vercel.app/backend/health → {"status":"UP"}

상세: 백엔드 레포 src/main/docs/프론트_API_연동_가이드.md
```

---

## 9. 백엔드 참고 (프론트 작업 불필요)

| 항목 | 값 |
|------|-----|
| EB 환경 | `Pintrip-develop-env-1` |
| 리전 | `ap-northeast-2` |
| CORS env | `CORS_ALLOWED_ORIGINS` = `https://pintrip-fe.vercel.app,http://localhost:5173,http://localhost:3000` |
