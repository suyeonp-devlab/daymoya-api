/** 일정 */
CREATE TABLE tasks (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(20) NOT NULL,
  description VARCHAR(100),
  status VARCHAR(20) NOT NULL DEFAULT 'TODO',
  priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
  start_at TIMESTAMPTZ,
  end_at TIMESTAMPTZ NOT NULL,
  completed_at TIMESTAMPTZ,
  category_id BIGINT,
  assignee_id BIGINT NOT NULL,
  group_id BIGINT,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at TIMESTAMPTZ,
  deleted_at TIMESTAMPTZ,
  CONSTRAINT fk_tasks_category FOREIGN KEY (category_id) REFERENCES categories(id),
  CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users(id)
);

-- TODO 그룹 fk 추가
-- ALTER TABLE tasks ADD CONSTRAINT fk_tasks_group FOREIGN KEY (group_id) REFERENCES groups(id);

-- 조회 성능용 인덱스
CREATE INDEX ix_tasks_assignee_status ON tasks (assignee_id, status) WHERE deleted_at IS NULL;
CREATE INDEX ix_tasks_group_status ON tasks (group_id, status) WHERE deleted_at IS NULL;

COMMENT ON TABLE tasks IS '일정';

COMMENT ON COLUMN tasks.id IS 'PK';
COMMENT ON COLUMN tasks.title IS '일정명';
COMMENT ON COLUMN tasks.description IS '일정설명';
COMMENT ON COLUMN tasks.status IS '상태(공통코드 TASK-001)';
COMMENT ON COLUMN tasks.priority IS '우선순위(공통코드 TASK-002)';
COMMENT ON COLUMN tasks.start_at IS '시작일시';
COMMENT ON COLUMN tasks.end_at IS '종료일시';
COMMENT ON COLUMN tasks.completed_at IS '완료일시';
COMMENT ON COLUMN tasks.category_id IS '카테고리 ID';
COMMENT ON COLUMN tasks.assignee_id IS '담당자 ID';
COMMENT ON COLUMN tasks.group_id IS '그룹 ID';
COMMENT ON COLUMN tasks.created_by IS '생성자';
COMMENT ON COLUMN tasks.created_at IS '생성일시';
COMMENT ON COLUMN tasks.updated_by IS '수정자';
COMMENT ON COLUMN tasks.updated_at IS '수정일시';
COMMENT ON COLUMN tasks.deleted_at IS '삭제일시';

-- 인덱스 코멘트
COMMENT ON INDEX ix_tasks_assignee_status IS '담당자 기준 상태별 일정 인덱스';
COMMENT ON INDEX ix_tasks_group_status IS '그룹 기준 상태별 일정 인덱스';