CREATE TABLE schedule_spaces (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255) NULL,
  space_type VARCHAR(20) NOT NULL,
  space_color VARCHAR(20) NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NULL
);

COMMENT ON TABLE schedule_spaces IS '스케줄 공간 정보 테이블';

COMMENT ON COLUMN schedule_spaces.id IS '스케줄 공간 PK';
COMMENT ON COLUMN schedule_spaces.name IS '스케줄 공간 이름';
COMMENT ON COLUMN schedule_spaces.description IS '스케줄 공간 설명';
COMMENT ON COLUMN schedule_spaces.space_type IS '스케줄 공간 유형 (PERSONAL 개인, GROUP 그룹)';
COMMENT ON COLUMN schedule_spaces.space_color IS '스케줄 공간 색상 (HEX 코드)';
COMMENT ON COLUMN schedule_spaces.status IS '스케줄 공간 상태 (ACTIVE 활성, DELETED 삭제)';
COMMENT ON COLUMN schedule_spaces.created_at IS '생성 시각';
COMMENT ON COLUMN schedule_spaces.updated_at IS '수정 시각';