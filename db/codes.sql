/** 공통코드 상세 */
CREATE TABLE codes (
  id BIGSERIAL PRIMARY KEY,
  group_id BIGINT NOT NULL,
  code VARCHAR(20) NOT NULL,
  code_name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  etc1 VARCHAR(255),
  etc2 VARCHAR(255),
  etc3 VARCHAR(255),
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  sort_no INTEGER NOT NULL DEFAULT 0,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at TIMESTAMPTZ,
  CONSTRAINT fk_codes_group FOREIGN KEY (group_id) REFERENCES code_groups(id),
  CONSTRAINT uk_codes_group_code UNIQUE (group_id, code)
);

COMMENT ON TABLE codes IS '공통코드 상세';

COMMENT ON COLUMN codes.id IS 'PK';
COMMENT ON COLUMN codes.group_id IS '그룹코드 ID';
COMMENT ON COLUMN codes.code IS '코드';
COMMENT ON COLUMN codes.code_name IS '코드명';
COMMENT ON COLUMN codes.description IS '코드설명';
COMMENT ON COLUMN codes.etc1 IS '기타1';
COMMENT ON COLUMN codes.etc2 IS '기타2';
COMMENT ON COLUMN codes.etc3 IS '기타3';
COMMENT ON COLUMN codes.enabled IS '사용여부';
COMMENT ON COLUMN codes.sort_no IS '정렬순서';
COMMENT ON COLUMN codes.created_by IS '생성자';
COMMENT ON COLUMN codes.created_at IS '생성일시';
COMMENT ON COLUMN codes.updated_by IS '수정자';
COMMENT ON COLUMN codes.updated_at IS '수정일시';
