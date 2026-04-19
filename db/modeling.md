## 데이모아 DB 설계

### 설계 원칙
- 개인 일정 중심 → 그룹 확장 가능 구조  
- Task 하나로 개인/그룹 일정 모두 처리  
- 최소 필드로 시작, 확장 가능하게 설계  
- 모든 도메인은 User 기준으로 연결  

---

### 1. users (서비스 사용자)

### 컬럼

| 컬럼명        | 타입        | 설명                    |
|------------|-----------|-----------------------|
| id         | uuid      | 사용자 ID                |
| email      | varchar   | 이메일 (로그인용)            |
| password   | varchar   | 비밀번호                  |
| nickname   | varchar   | 사용자 닉네임               |
| status     | varchar   | 상태 (ACTIVE, INACTIVE) |
| created_at | timestamp | 생성일                   |
| updated_at | timestamp | 수정일                   |

---

### 2. tasks (일정/할 일)

### 컬럼

| 컬럼명          | 타입              | 설명              |
|--------------|-----------------|-----------------|
| id           | uuid            | 일정 ID           |
| title        | varchar         | 제목              |
| description  | text            | 설명              |
| status       | varchar         | 상태 (TODO, DONE) |
| due_at       | timestamp       | 마감일             |
| completed_at | timestamp       | 완료일             |
| created_by   | uuid            | 생성자 (user_id)   |
| assignee_id  | uuid            | 담당자 (user_id)   |
| group_id     | uuid (nullable) | 그룹 ID           |
| created_at   | timestamp       | 생성일             |
| updated_at   | timestamp       | 수정일             |

---

### 3. groups (사용자 그룹)

### 컬럼

| 컬럼명         | 타입        | 설명    |
|-------------|-----------|-------|
| id          | uuid      | 그룹 ID |
| name        | varchar   | 그룹 이름 |
| description | text      | 설명    |
| created_by  | uuid      | 생성자   |
| created_at  | timestamp | 생성일   |
| updated_at  | timestamp | 수정일   |

---

### 4. group_members (그룹 멤버십 User ↔ Group 관계)

### 컬럼

| 컬럼명       | 타입        | 설명                 |
|-----------|-----------|--------------------|
| id        | uuid      | PK                 |
| group_id  | uuid      | 그룹 ID              |
| user_id   | uuid      | 사용자 ID             |
| role      | varchar   | 역할 (OWNER, MEMBER) |
| joined_at | timestamp | 참여일                |

---

## 설계 포인트

### 1. 개인 일정 vs 그룹 일정
- group_id NULL → 개인 일정
- group_id 있음 → 그룹 일정

### 2. 할당 구조
- created_by: 누가 만들었는지
- assignee_id: 누가 해야 하는지

---

### 3. 상태 관리
- TODO
- DONE

(추후 확장 가능: IN_PROGRESS 등)

---

### 4. 확장 고려 (지금은 안 만듦)
- notifications
- comments
- task_history
- attachments
