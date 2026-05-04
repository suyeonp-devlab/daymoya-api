/** 카테고리 */
CREATE TABLE categories (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(10) NOT NULL,
  scope VARCHAR(20) NOT NULL,
  scope_user_id BIGINT,
  scope_group_id BIGINT,
  color VARCHAR(7),
  sort_no INTEGER NOT NULL DEFAULT 0,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at TIMESTAMPTZ,
  CONSTRAINT fk_categories_user FOREIGN KEY (scope_user_id) REFERENCES users(id)
);

-- TODO 그룹 fk 추가
-- ALTER TABLE categories ADD CONSTRAINT fk_categories_group FOREIGN KEY (scope_group_id) REFERENCES groups(id);

-- 조회 성능용 인덱스
CREATE INDEX ix_categories_personal ON categories (scope, scope_user_id);
CREATE INDEX ix_categories_group ON categories (scope, scope_group_id);

COMMENT ON TABLE categories IS '카테고리';

COMMENT ON COLUMN categories.id IS 'PK';
COMMENT ON COLUMN categories.name IS '카테고리명';
COMMENT ON COLUMN categories.scope IS '범위(공통코드 CATEGORY-001)';
COMMENT ON COLUMN categories.scope_user_id IS '사용자 ID';
COMMENT ON COLUMN categories.scope_group_id IS '그룹 ID';
COMMENT ON COLUMN categories.color IS '색상 (HEX 6자리 형식, 예: #FFFFFF)';
COMMENT ON COLUMN categories.sort_no IS '정렬순서';
COMMENT ON COLUMN categories.created_by IS '생성자';
COMMENT ON COLUMN categories.created_at IS '생성일시';
COMMENT ON COLUMN categories.updated_by IS '수정자';
COMMENT ON COLUMN categories.updated_at IS '수정일시';

COMMENT ON INDEX ix_categories_personal IS '개인 카테고리 인덱스';
COMMENT ON INDEX ix_categories_group IS '그룹 카테고리 인덱스';