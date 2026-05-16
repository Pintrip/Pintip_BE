# Pintrip Backend

핀트립(Pintrip) 서비스의 Spring Boot 백엔드 API입니다.  
로그인 없이 **세션 UUID(`sessionId`)** 로 여행 상태(동네·이미지 카드·퀘스트·후기)를 유지합니다.

| 항목 | 내용 |
|------|------|
| 프레임워크 | Spring Boot 3.5 · Java 17 |
| DB | MySQL 8 (로컬: Docker Compose) |
| API 문서 | Swagger UI |
| 개발 배포 | AWS Elastic Beanstalk (`ap-northeast-2`) |
| 프론트 | [pintrip-fe](https://pintrip-fe.vercel.app) (Vercel) |

---

## 주요 기능

- **동네·콘텐츠**: 활성 동네 10개, 동네별 이미지 카드 3장, 카드당 퀘스트 3개
- **여행 세션**: `POST /trip-sessions` 로 `sessionId` 발급 → 후기·완료·복구
- **세션 만료**: 생성 후 **2일** (`TripSessionPolicy.EXPIRE_DAYS`)
- **자동 완료**: 퀘스트 후기 3건 저장 시 `status` → `COMPLETED`

---

## 빠른 시작 (로컬)

### 사전 요구사항

- JDK 17
- Docker (MySQL용)

### 실행

```bash
# 1. MySQL 기동 (호스트 3307 → 컨테이너 3306)
docker compose up -d

# 2. 애플리케이션 실행 (profile: local)
./gradlew bootRun
```

- API: http://localhost:8080  
- Swagger: http://localhost:8080/swagger-ui.html  
- 헬스: http://localhost:8080/health  

`application-local.yaml` 에서 Spring Boot Docker Compose가 MySQL을 자동 기동할 수도 있습니다.  
호스트 **3306** 에 다른 MySQL이 있으면 Compose는 **3307** 로 매핑합니다 (`compose.yaml` 참고).

### DB 초기화

최초 `docker compose up` 시 `schema.sql`, `data.sql` 이 자동 적용됩니다.  
수동·RDS 작업은 [`src/main/resources/db/README.md`](src/main/resources/db/README.md) 를 따릅니다.

---

## API 개요

상세 스펙·요청/응답 예시는 **Swagger** 또는 [`src/main/docs/백엔드_개발_실행계획.md`](src/main/docs/백엔드_개발_실행계획.md) 를 참고하세요.

### 권장 플로우

```text
GET  /dongs
GET  /dongs/{dongId}/image-cards
POST /trip-sessions                    { dongId, imageCardId } → sessionId
PUT  /trip-sessions/{sessionId}/quest-reviews/{questId}
GET  /trip-sessions/{sessionId}        새로고침·복구
PATCH /trip-sessions/{sessionId}/complete   (선택, 수동 완료)
```

### P0 엔드포인트

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/dongs` | 활성 동네 목록 |
| `GET` | `/dongs/{dongId}/image-cards` | 카드 3개 + 카드별 퀘스트 3개 |
| `POST` | `/trip-sessions` | 세션 생성 → `sessionId` |
| `GET` | `/trip-sessions/{sessionId}` | 세션 조회·복구 |
| `GET` | `/trip-sessions` | 동일 (헤더 `X-Session-Id`) |
| `PUT` | `/trip-sessions/{sessionId}/quest-reviews/{questId}` | 퀘스트 후기 저장 |
| `GET` | `/trip-sessions/{sessionId}/quest-reviews` | 후기 목록 |
| `PATCH` | `/trip-sessions/{sessionId}/complete` | 수동 완료 |
| `GET` | `/health` | 헬스 체크 |

퀘스트 완료 여부(`isCompleted`)는 **`GET /trip-sessions/{sessionId}`** 응답의 `selectedImageCard`·`reviews` 기준입니다.

---

## 프로젝트 구조

```text
src/main/java/pintrip/
├── PintripApplication.java
├── global/          # CORS, Swagger, 예외 처리, /health
├── domain/dong/     # 동네 API
├── domain/image/    # 이미지 카드·퀘스트 엔티티
└── domain/session/  # 세션·후기 API

src/main/resources/
├── application.yaml
├── application-local.yaml
├── application-prod.yaml
└── db/              # schema.sql, data.sql

src/main/docs/       # 상세 설계·연동 문서
```

운영 코드는 `pintrip.domain.*` 기준입니다. `pintrip/demo/**` 는 초기 스캐폴드입니다.

---

## 설정

| 프로필 | 용도 | 파일 |
|--------|------|------|
| `local` (기본) | 로컬 개발 | `application-local.yaml` |
| `prod` | Elastic Beanstalk | `application-prod.yaml` + EB 환경 변수 |

운영 시 EB에서 `SPRING_PROFILES_ACTIVE=prod`, RDS·CORS 변수를 주입합니다.  
체크리스트: [`src/main/docs/배포_EB_체크리스트.md`](src/main/docs/배포_EB_체크리스트.md)

### CORS

`app.cors.allowed-origins` — 로컬은 Vite(`5173`) 등, 운영은 Vercel 프론트 URL.

---

## 테스트

```bash
./gradlew test
```

---

## 배포 (CI/CD)

`main` 브랜치에 **push** 하면 GitHub Actions가 빌드 후 Elastic Beanstalk(`pintrip-develop`)에 배포합니다.

- 워크플로: [`.github/workflows/dev_depoly.yml`](.github/workflows/dev_depoly.yml)
- 수동 실행: Actions → **Dev CI/CD** → **Run workflow**

프론트는 Vercel에서 `/backend` 로 이 API를 프록시합니다.  
연동 방법: [`src/main/docs/프론트_API_연동_가이드.md`](src/main/docs/프론트_API_연동_가이드.md)

---

## 문서

| 문서 | 내용 |
|------|------|
| [`src/main/docs/README.md`](src/main/docs/README.md) | 문서 인덱스 |
| [`src/main/docs/백엔드_개발_실행계획.md`](src/main/docs/백엔드_개발_실행계획.md) | API·플로우·DoD (메인) |
| [`src/main/docs/핀트립_ERD.md`](src/main/docs/핀트립_ERD.md) | 테이블·API↔DB |
| [`src/main/docs/백엔드_폴더_구조.md`](src/main/docs/백엔드_폴더_구조.md) | 패키지·수정 경로 |
| [`src/main/docs/프론트_API_연동_가이드.md`](src/main/docs/프론트_API_연동_가이드.md) | Vercel·환경 변수 |
| [`src/main/resources/db/README.md`](src/main/resources/db/README.md) | DDL·seed·RDS |

---

## 라이선스

프로젝트 정책에 따릅니다.
