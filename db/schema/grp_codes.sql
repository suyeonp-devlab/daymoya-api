CREATE TABLE grp_codes (
  id BIGSERIAL PRIMARY KEY,
  grp_code_id VARCHAR(20) NOT NULL UNIQUE,
  grp_code_name VARCHAR(100) NOT NULL,
  description TEXT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NULL
);

COMMENT ON TABLE grp_codes IS '공통코드 그룹 테이블';

COMMENT ON COLUMN grp_codes.id IS '그룹코드 PK';
COMMENT ON COLUMN grp_codes.grp_code_id IS '그룹코드 ID';
COMMENT ON COLUMN grp_codes.grp_code_name IS '그룹코드 이름';
COMMENT ON COLUMN grp_codes.description IS '설명';
COMMENT ON COLUMN grp_codes.use_yn IS '사용 여부 (Y/N)';
COMMENT ON COLUMN grp_codes.created_at IS '생성 시각';
COMMENT ON COLUMN grp_codes.updated_at IS '수정 시각';