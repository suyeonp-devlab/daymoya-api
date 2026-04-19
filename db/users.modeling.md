## 데이모아 users 테이블 설계

### 설계 전제

- 로그인 방식은 이메일 + 비밀번호만 사용한다.
- 회원가입 시 이메일 인증이 필수다.
- 이메일은 로그인 ID이며, 기본적으로 유니크하게 관리한다.
- 탈퇴는 soft delete로 처리한다.
- 탈퇴 후 동일 이메일 재가입이 가능해야 하므로, 활성 사용자 기준 email unique 정책이 필요하다.
- 사용자 표시 이름은 nickname을 사용한다.
- 사용자 상태는 활성 / 탈퇴 / 정지를 가진다.
- 정지 사용자는 로그인이 불가하다.
- 글로벌 확장은 고려하지 않으며 timezone은 Asia/Seoul 고정 정책으로 간다.

---

### 테이블명
`users`

### 컬럼 설계

| 컬럼명                 | 타입           | NULL | 설명                                    |
|---------------------|--------------|------|---------------------------------------|
| id                  | uuid         | N    | 사용자 PK                                |
| email               | varchar(255) | N    | 로그인 ID 이메일                            |
| password            | varchar(255) | N    | 해시된 비밀번호                              |
| nickname            | varchar(50)  | N    | 사용자 닉네임                               |
| profile_image_path  | varchar(500) | Y    | 프로필 이미지 URL                           |
| status              | varchar(20)  | N    | 사용자 상태 (ACTIVE, WITHDRAWN, SUSPENDED) |
| failed_login_count  | integer      | N    | 연속 로그인 실패 횟수                          |
| password_changed_at | timestamp    | Y    | 마지막 비밀번호 변경 시각                        |
| last_login_at       | timestamp    | Y    | 마지막 로그인 시각                            |
| created_at          | timestamp    | N    | 생성 시각                                 |
| updated_at          | timestamp    | N    | 수정 시각                                 |
| deleted_at          | timestamp    | Y    | 탈퇴 시각 (soft delete)                   |

---

### 컬럼별 실무 메모

### 1. id

- UUID PK 사용
- 외부 도메인(task, group, membership)에서 참조하는 기준 키

### 2. email

- 로그인 ID로 사용
- 화면 표시는 필요 시 마스킹해서 사용
- 그룹 초대 시 사용자 식별 입력값으로 사용
- soft delete + 재가입 정책 때문에 활성 사용자 기준 unique가 필요함

### 3. password

- 평문 저장 금지
- bcrypt/argon2 등 해시 저장

### 4. nickname

- 서비스 내 기본 표시 이름
- group, task 화면에서 사용자 식별용으로 사용
- 별도 name 컬럼은 두지 않음

### 5. profile_image_path

- 최초 회원가입 시 랜덤 프로필 이미지 지급
- 마이페이지에서 변경 가능

### 6. status

- ACTIVE: 정상 사용자
- WITHDRAWN: 탈퇴 사용자
- SUSPENDED: 정지 사용자

### 7. failed_login_count

- 로그인 실패 시 증가
- 로그인 성공 시 0으로 초기화

### 8. password_changed_at

- 비밀번호 변경 권장 팝업 기준 컬럼
- 6개월 경과 시 로그인 후 변경 권장 팝업 노출

### 9. last_login_at

- 통계, 운영, 보안 확인 용도

### 10. deleted_at

- soft delete 기준 컬럼
- 탈퇴 사용자 일정/그룹 이력 보존을 위해 필요
- 탈퇴 여부 판별은 status = WITHDRAWN와 함께 사용
