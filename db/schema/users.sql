/** 사용자 정보 테이블 */
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  profile_image_path VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  failed_login_count INTEGER NOT NULL DEFAULT 0,
  password_changed_at TIMESTAMPTZ,
  last_login_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ,
  deleted_at TIMESTAMPTZ,
  CONSTRAINT ck_users_status CHECK (status IN ('ACTIVE', 'WITHDRAWN', 'SUSPENDED')),
  CONSTRAINT ck_users_failed_login_count CHECK (failed_login_count >= 0),
  CONSTRAINT ck_users_email_lowercase CHECK (email = LOWER(email))
);

-- 활성 사용자 기준 이메일 유니크
CREATE UNIQUE INDEX ux_users_email_active ON users (email) WHERE deleted_at IS NULL;

-- 조회 성능용 인덱스
CREATE INDEX ix_users_status ON users (status);
CREATE INDEX ix_users_created_at ON users (created_at);

-- 테이블 코멘트
COMMENT ON TABLE users IS '사용자 정보 테이블';

-- 컬럼 코멘트
COMMENT ON COLUMN users.id IS '사용자 PK';
COMMENT ON COLUMN users.email IS '이메일 (로그인 ID)';
COMMENT ON COLUMN users.password IS '비밀번호';
COMMENT ON COLUMN users.nickname IS '닉네임';
COMMENT ON COLUMN users.profile_image_path IS '프로필 이미지 경로';
COMMENT ON COLUMN users.status IS '사용자 상태';
COMMENT ON COLUMN users.failed_login_count IS '연속 로그인 실패 횟수';
COMMENT ON COLUMN users.password_changed_at IS '마지막 비밀번호 변경 시각';
COMMENT ON COLUMN users.last_login_at IS '마지막 로그인 시각';
COMMENT ON COLUMN users.created_at IS '생성 시각';
COMMENT ON COLUMN users.updated_at IS '수정 시각';
COMMENT ON COLUMN users.deleted_at IS '탈퇴 시각';

-- 인덱스 코멘트
COMMENT ON INDEX ux_users_email_active IS '활성 사용자 기준 이메일 유니크 인덱스';
COMMENT ON INDEX ix_users_status IS '사용자 상태 조회 인덱스';
COMMENT ON INDEX ix_users_created_at IS '사용자 생성일 조회 인덱스';