# Elastic Beanstalk 배포 체크리스트 (백엔드)

배포 전·후에 아래 항목을 순서대로 확인한다.  
`[직접 입력]` 표시는 **레포에 넣지 말고** AWS 콘솔·GitHub Secrets에만 넣는다.

---

## 1. AWS Elastic Beanstalk 환경 속성

**EB 콘솔 → 환경 → 구성 → 소프트웨어 → 환경 속성**

| 키 | 설명 | 값 (Pintrip develop) |
|----|------|----------------------|
| `SPRING_PROFILES_ACTIVE` | 운영 프로필 | `prod` |
| `RDS_HOSTNAME` | RDS 엔드포인트 호스트 | `pintrip-db.cfeugo8mgi26.ap-northeast-2.rds.amazonaws.com` |
| `RDS_PORT` | MySQL 포트 | `3306` |
| `RDS_DB_NAME` | MySQL **데이터베이스** 이름 (인스턴스 식별자와 동일할 수 있음) | `pintrip-db` |
| `RDS_USERNAME` | DB 사용자 | `admin` |
| `RDS_PASSWORD` | DB 비밀번호 | `[직접 입력]` EB 환경 속성에만 |
| `CORS_ALLOWED_ORIGINS` | 프론트 URL (콤마 구분, 끝 `/` 없음) | 아래 권장값 |

**권장값:**

```text
https://pintrip-fe.vercel.app,http://localhost:5173,http://localhost:3000
```

> EB에 RDS를 **연동**해 두면 `RDS_*` 변수가 자동 생성되는 경우가 있다. 콘솔에 이미 있으면 중복 입력하지 않아도 된다.

---

## 2. RDS / 스키마 (JPA `ddl-auto: update`)

- `createDatabaseIfNotExist=true`: **DB 이름** 없으면 앱 기동 시 생성
- `ddl-auto: update`: **`@Entity` 추가·수정 시** 테이블/컬럼 자동 반영 (삭제는 보장 안 됨)
- `schema.sql` / `data.sql`: **로컬 Docker init 전용** — EB 배포 시 자동 실행 안 함

**최초 EB 배포 후:** `@Entity`가 있으면 재배포만으로 테이블 생성 (`trip_session_quest_reviews` 포함).  
**목데이터(`data.sql`)** 는 RDS에 수동 1회 또는 로컬에서 export 후 import.

기존 RDS에 `places`/`quests` 등 구 테이블이 남아 있어도 앱은 무시한다. 정리는 선택 사항.

### DB 이름만 없을 때 (Unknown database)

EB 재배포(`createDatabaseIfNotExist`) 또는 SSH에서:

```sql
CREATE DATABASE IF NOT EXISTS `pintrip-db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

`RDS_DB_NAME`과 동일한 이름으로 생성.

---

## 3. 보안 그룹

| 연결 | 확인 |
|------|------|
| EB EC2 → RDS | RDS SG 인바운드에 EB 인스턴스 SG, 포트 3306 |
| 인터넷 → EB ALB | 80(HTTP), HTTPS 사용 시 443 |
| RDS 3306 | **퍼블릭 개방하지 않음** |

---

## 4. GitHub Actions Secrets

`.github/workflows/dev_depoly.yml` 배포용:

| Secret | 설명 |
|--------|------|
| `AWS_ACTION_ACCESS_KEY_ID` | `[직접 입력]` 배포용 IAM Access Key |
| `AWS_ACTION_SECRET_ACCESS_KEY` | `[직접 입력]` Secret Key |

워크플로에 이미 적힌 값 (필요 시 수정):

| 항목 | 현재 값 |
|------|---------|
| `application_name` | `pintrip-develop` |
| `environment_name` | `Pintrip-develop-env-1` |
| `region` | `ap-northeast-2` |

---

## 5. 502 Bad Gateway (Actions 성공인데 URL 502)

**의미:** ALB/nginx는 살아 있지만 **뒤 Spring 앱이 5000에서 안 떠 있음** (기동 실패·즉시 종료).

### 5-1. EB 로그 확인 (가장 먼저)

EB 콘솔 → 환경 → **로그** → **요청** 또는 **전체 로그** 다운로드 후 `web.stdout.log` / `eb-engine.log` 에서:

- `Could not resolve placeholder 'CORS_ALLOWED_ORIGINS'` → 환경 속성 누락
- `Communications link failure` / `Connection timed out` → **RDS 보안 그룹** (EB SG → 3306)
- `Access denied for user` → `RDS_USERNAME` / `RDS_PASSWORD` / DB 이름 불일치
- `Unknown database` → `RDS_DB_NAME` 확인 (`pintrip-db`)

### 5-2. 필수 환경 속성 재확인

| 키 | 없으면 |
|----|--------|
| `RDS_HOSTNAME`, `RDS_DB_NAME`, `RDS_USERNAME`, `RDS_PASSWORD` | 기동 실패 → 502 |
| `CORS_ALLOWED_ORIGINS` | (기본값 있음) 누락 시 예전 JAR는 실패 가능 |

### 5-3. RDS 보안 그룹

RDS 인바운드 **3306** 소스 = **Elastic Beanstalk 환경의 EC2 보안 그룹** (내 맥 IP 아님).

### 5-4. 재배포

코드/설정 수정 후 GitHub Actions **Run workflow** 또는 zip 재업로드.

---

## 6. 배포 후 확인

```bash
# 헬스체크
curl http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com/health
# (선택) Actuator: /actuator/health — prod 프로필 + 재배포 후

# API (구현 후)
curl http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com/dongs
```

EB 환경 URL: `http://Pintrip-develop-env-1.eba-kzgyhpvp.ap-northeast-2.elasticbeanstalk.com`

---

## 7. 관련 파일

| 파일 | 역할 |
|------|------|
| `application-prod.yaml` | 포트 5000, RDS, CORS, Compose 비활성 |
| `Procfile` | `application.jar` + `prod` 프로필 |
| `.ebextensions/01_healthcheck.config` | ALB 헬스체크 `/actuator/health` |
| `.env.example` | 환경 변수 키 목록 (값은 커밋 금지) |

프론트 연동: `프론트_API_연동_가이드.md`
