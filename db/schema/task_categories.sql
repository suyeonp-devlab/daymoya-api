/** 일정 카테고리 테이블 */
CREATE TABLE task_categories (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  scope_type VARCHAR(20) NOT NULL,
  scope_user_id BIGINT,
  scope_group_id BIGINT,
  color VARCHAR(7),
  display_order INTEGER NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ,
  CONSTRAINT fk_task_categories_user FOREIGN KEY (scope_user_id) REFERENCES users(id),
  CONSTRAINT ck_task_categories_scope_type CHECK (scope_type IN ('SYSTEM_PERSONAL', 'PERSONAL', 'SYSTEM_GROUP', 'GROUP'))
);

-- (추후) 그룹 fk
-- ALTER TABLE task_categories ADD CONSTRAINT fk_task_categories_group FOREIGN KEY (scope_group_id) REFERENCES groups(id);

-- 조회 성능용 인덱스
CREATE INDEX ix_task_categories_personal ON task_categories (scope_type, scope_user_id);
CREATE INDEX ix_task_categories_group ON task_categories (scope_type, scope_group_id);

-- 테이블 코멘트
COMMENT ON TABLE task_categories IS '일정 카테고리 테이블';

-- 컬럼 코멘트
COMMENT ON COLUMN task_categories.id IS '카테고리 PK';
COMMENT ON COLUMN task_categories.name IS '카테고리 이름';
COMMENT ON COLUMN task_categories.scope_type IS '카테고리 범위';
COMMENT ON COLUMN task_categories.scope_user_id IS '사용자 ID (users.id)';
COMMENT ON COLUMN task_categories.scope_group_id IS '그룹 ID (groups.id)';
COMMENT ON COLUMN task_categories.color IS '카테고리 색상 (HEX 6자리 형식, 예: #FFFFFF)';
COMMENT ON COLUMN task_categories.display_order IS '카테고리 표시 순서';
COMMENT ON COLUMN task_categories.created_at IS '생성 시각';
COMMENT ON COLUMN task_categories.updated_at IS '수정 시각';

-- 인덱스 코멘트
COMMENT ON INDEX ix_task_categories_personal IS '개인 카테고리 조회 인덱스';
COMMENT ON INDEX ix_task_categories_group IS '그룹 카테고리 조회 인덱스';