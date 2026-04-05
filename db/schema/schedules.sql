CREATE TABLE schedules (
  id BIGSERIAL PRIMARY KEY,
  schedule_space_id BIGINT NOT NULL,
  created_member_id BIGINT NOT NULL,
  assignee_member_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  description TEXT NULL,
  start_at TIMESTAMP NOT NULL,
  end_at TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
  completed_at TIMESTAMP NULL,
  canceled_at TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NULL,
  CONSTRAINT fk_schedules_space FOREIGN KEY (schedule_space_id) REFERENCES schedule_spaces(id),
  CONSTRAINT fk_schedules_created_member FOREIGN KEY (created_member_id) REFERENCES members(id),
  CONSTRAINT fk_schedules_assignee_member FOREIGN KEY (assignee_member_id) REFERENCES members(id)
);

CREATE INDEX idx_schedules_space_start_at ON schedules (schedule_space_id, start_at);
CREATE INDEX idx_schedules_space_end_at ON schedules (schedule_space_id, end_at);
CREATE INDEX idx_schedules_assignee_member_id ON schedules (assignee_member_id);

COMMENT ON TABLE schedules IS '확정 스케줄 정보 테이블';

COMMENT ON COLUMN schedules.id IS '스케줄 PK';
COMMENT ON COLUMN schedules.schedule_space_id IS '스케줄 공간 ID';
COMMENT ON COLUMN schedules.created_member_id IS '스케줄 생성 회원 ID';
COMMENT ON COLUMN schedules.assignee_member_id IS '스케줄 담당 회원 ID';
COMMENT ON COLUMN schedules.title IS '스케줄 제목';
COMMENT ON COLUMN schedules.description IS '스케줄 설명';
COMMENT ON COLUMN schedules.start_at IS '시작 일시';
COMMENT ON COLUMN schedules.end_at IS '종료 일시';
COMMENT ON COLUMN schedules.status IS '스케줄 상태 (IN_PROGRESS 진행중, COMPLETED 완료, CANCELED 삭제)';
COMMENT ON COLUMN schedules.completed_at IS '완료 시각';
COMMENT ON COLUMN schedules.canceled_at IS '삭제 시각';
COMMENT ON COLUMN schedules.created_at IS '생성 시각';
COMMENT ON COLUMN schedules.updated_at IS '수정 시각';