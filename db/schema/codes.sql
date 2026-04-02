CREATE TABLE codes (
  id BIGSERIAL PRIMARY KEY,
  grp_code_id VARCHAR(20) NOT NULL,
  code VARCHAR(20) NOT NULL,
  code_name VARCHAR(100) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  description TEXT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NULL,
  CONSTRAINT fk_codes_grp FOREIGN KEY (grp_code_id) REFERENCES grp_codes (grp_code_id),
  CONSTRAINT uk_codes_grp_code UNIQUE (grp_code_id, code)
);

COMMENT ON TABLE codes IS '공통코드 상세 테이블';

COMMENT ON COLUMN codes.id IS '공통코드 PK';
COMMENT ON COLUMN codes.grp_code_id IS '그룹코드 ID';
COMMENT ON COLUMN codes.code IS '코드 값';
COMMENT ON COLUMN codes.code_name IS '코드 이름';
COMMENT ON COLUMN codes.sort_order IS '정렬 순서';
COMMENT ON COLUMN codes.description IS '설명';
COMMENT ON COLUMN codes.use_yn IS '사용 여부 (Y/N)';
COMMENT ON COLUMN codes.created_at IS '생성 시각';
COMMENT ON COLUMN codes.updated_at IS '수정 시각';