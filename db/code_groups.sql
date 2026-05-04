/** 공통코드 그룹 */
CREATE TABLE code_groups (
  id BIGSERIAL PRIMARY KEY,
  group_code VARCHAR(20) NOT NULL UNIQUE,
  group_name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  domain VARCHAR(20) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at TIMESTAMPTZ
);

COMMENT ON TABLE code_groups IS '공통코드 그룹';

COMMENT ON COLUMN code_groups.id IS 'PK';
COMMENT ON COLUMN code_groups.group_code IS '그룹코드';
COMMENT ON COLUMN code_groups.group_name IS '그룹명';
COMMENT ON COLUMN code_groups.description IS '그룹설명';
COMMENT ON COLUMN code_groups.domain IS '업무(공통코드 CMN-001)';
COMMENT ON COLUMN code_groups.enabled IS '사용여부';
COMMENT ON COLUMN code_groups.created_by IS '생성자';
COMMENT ON COLUMN code_groups.created_at IS '생성일시';
COMMENT ON COLUMN code_groups.updated_by IS '수정자';
COMMENT ON COLUMN code_groups.updated_at IS '수정일시';
