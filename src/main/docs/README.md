# 핀트립 문서 인덱스

문서 역할을 나눠 **중복 없이** 참고한다. API·DB의 단일 기준(Single Source of Truth)은 아래 표의 **굵은 문서**다.

| 문서 | 대상 | 내용 |
|------|------|------|
| **[백엔드_개발_실행계획.md](백엔드_개발_실행계획.md)** | 백엔드 | **메인** — 플로우, P0 API, 도메인, 에러, DoD |
| **[핀트립_ERD.md](핀트립_ERD.md)** | 백엔드·DB | 테이블 관계, API↔DB 매핑 |
| **[백엔드_폴더_구조.md](백엔드_폴더_구조.md)** | 백엔드 | 패키지 트리, 수정 경로 |
| **[프론트_API_연동_가이드.md](프론트_API_연동_가이드.md)** | 프론트 | Vercel proxy, 환경 변수, API 목록, 트러블슈팅 |
| **[배포_EB_체크리스트.md](배포_EB_체크리스트.md)** | 배포 | EB·RDS·CORS·502 |
| [../resources/db/README.md](../resources/db/README.md) | DB | DDL/seed, 로컬 Docker 초기화 |
| [핀트립_아키텍처.md](핀트립_아키텍처.md) | 배경 | 세션(UUID) 설계 이유 (API 상세 없음) |
| [핀트립_기획.md](핀트립_기획.md) | 기획 | 서비스 콘셉트·UX 의도 (구현과 일부 상이) |

---

## 빠른 링크

- Swagger (로컬): `http://localhost:8080/swagger-ui.html`
- 프론트 연동 베이스: `/backend` (Vercel rewrite)
- 세션 만료: 생성 후 **2일** (`TripSessionPolicy.EXPIRE_DAYS`)

---

## GitHub 템플릿

| 경로 | 용도 |
|------|------|
| `.github/ISSUE_TEMPLATE/backend-p0.md` | P0 이슈 |
| `.github/pull_request_template.md` | PR 체크리스트 |
