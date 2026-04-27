/** 일정 테이블 */
CREATE TABLE tasks (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'TODO',
  start_at TIMESTAMPTZ,
  due_at TIMESTAMPTZ NOT NULL,
  completed_at TIMESTAMPTZ,
  category_id BIGINT,
  created_by BIGINT NOT NULL,
  assignee_id BIGINT NOT NULL,
  group_id BIGINT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ,
  deleted_at TIMESTAMPTZ,
  CONSTRAINT fk_tasks_created_by FOREIGN KEY (created_by) REFERENCES users(id),
  CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users(id),
  CONSTRAINT ck_tasks_status CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE', 'CANCELLED'))
);

-- 카테고리 fk
ALTER TABLE tasks ADD CONSTRAINT fk_tasks_category FOREIGN KEY (category_id) REFERENCES task_categories(id);

--(추후) 그룹 fk
-- ALTER TABLE tasks ADD CONSTRAINT fk_tasks_group FOREIGN KEY (group_id) REFERENCES groups(id);

-- 조회 성능용 인덱스
CREATE INDEX ix_tasks_assignee_status ON tasks (assignee_id, status) WHERE deleted_at IS NULL;
CREATE INDEX ix_tasks_group_status ON tasks (group_id, status) WHERE deleted_at IS NULL;
CREATE INDEX ix_tasks_assignee_due_at ON tasks (assignee_id, due_at) WHERE deleted_at IS NULL;
CREATE INDEX ix_tasks_category_id ON tasks (category_id) WHERE deleted_at IS NULL;

-- 테이블 코멘트
COMMENT ON TABLE tasks IS '일정 테이블';

-- 컬럼 코멘트
COMMENT ON COLUMN tasks.id IS '일정 PK';
COMMENT ON COLUMN tasks.title IS '일정 제목';
COMMENT ON COLUMN tasks.description IS '일정 설명';
COMMENT ON COLUMN tasks.status IS '일정 상태';
COMMENT ON COLUMN tasks.start_at IS '시작 시각';
COMMENT ON COLUMN tasks.due_at IS '마감 시각';
COMMENT ON COLUMN tasks.completed_at IS '완료 시각';
COMMENT ON COLUMN tasks.category_id IS '카테고리 ID (task_categories.id)';
COMMENT ON COLUMN tasks.created_by IS '생성자 ID (users.id)';
COMMENT ON COLUMN tasks.assignee_id IS '담당자 ID (users.id)';
COMMENT ON COLUMN tasks.group_id IS '그룹 ID (groups.id)';
COMMENT ON COLUMN tasks.created_at IS '생성 시각';
COMMENT ON COLUMN tasks.updated_at IS '수정 시각';
COMMENT ON COLUMN tasks.deleted_at IS '삭제 시각';

-- 인덱스 코멘트
COMMENT ON INDEX ix_tasks_assignee_status IS '담당자 기준 상태별 일정 조회 인덱스';
COMMENT ON INDEX ix_tasks_group_status IS '그룹 기준 상태별 일정 조회 인덱스';
COMMENT ON INDEX ix_tasks_assignee_due_at IS '담당자 기준 마감일 정렬 인덱스';
COMMENT ON INDEX ix_tasks_category_id IS '카테고리 기준 일정 조회 인덱스';