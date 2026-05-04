/** 사용자 정보 */
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  nickname VARCHAR(50) NOT NULL,
  profile_image_path VARCHAR(500),
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  failed_login_count INTEGER NOT NULL DEFAULT 0,
  password_changed_at TIMESTAMPTZ,
  last_login_at TIMESTAMPTZ,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at TIMESTAMPTZ,
  deleted_at TIMESTAMPTZ,
  CONSTRAINT ck_users_email_lowercase CHECK (email = LOWER(email))
);

COMMENT ON TABLE users IS '사용자';

COMMENT ON COLUMN users.id IS 'PK';
COMMENT ON COLUMN users.email IS '이메일(로그인 ID)';
COMMENT ON COLUMN users.password IS '비밀번호';
COMMENT ON COLUMN users.nickname IS '닉네임';
COMMENT ON COLUMN users.profile_image_path IS '프로필 이미지 경로';
COMMENT ON COLUMN users.role IS '권한(공통코드 USER-001)';
COMMENT ON COLUMN users.status IS '상태(공통코드 USER-002)';
COMMENT ON COLUMN users.failed_login_count IS '연속 로그인 실패 횟수';
COMMENT ON COLUMN users.password_changed_at IS '비밀번호 변경 시각';
COMMENT ON COLUMN users.last_login_at IS '로그인 시각';
COMMENT ON COLUMN users.created_by IS '생성자';
COMMENT ON COLUMN users.created_at IS '생성일시';
COMMENT ON COLUMN users.updated_by IS '수정자';
COMMENT ON COLUMN users.updated_at IS '수정일시';
COMMENT ON COLUMN users.deleted_at IS '탈퇴일시';