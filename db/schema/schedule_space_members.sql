CREATE TABLE schedule_space_members (
  id BIGSERIAL PRIMARY KEY,
  schedule_space_id BIGINT NOT NULL,
  member_id BIGINT NOT NULL,
  role VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  joined_at TIMESTAMP NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NULL,
  CONSTRAINT fk_ssm_space FOREIGN KEY (schedule_space_id) REFERENCES schedule_spaces(id),
  CONSTRAINT fk_ssm_member FOREIGN KEY (member_id) REFERENCES members(id),
  CONSTRAINT uk_ssm_space_member UNIQUE (schedule_space_id, member_id)
);

CREATE INDEX idx_ssm_member_id ON schedule_space_members (member_id);

COMMENT ON TABLE schedule_space_members IS '스케줄 공간 회원 정보 테이블';

COMMENT ON COLUMN schedule_space_members.id IS '스케줄 공간 회원 PK';
COMMENT ON COLUMN schedule_space_members.schedule_space_id IS '스케줄 공간 ID';
COMMENT ON COLUMN schedule_space_members.member_id IS '회원 ID';
COMMENT ON COLUMN schedule_space_members.role IS '스케줄 공간 내 역할 (OWNER 소유자, MEMBER 그룹원)';
COMMENT ON COLUMN schedule_space_members.status IS '스케줄 공간 회원 상태 (ACTIVE 활성, LEFT 자발적 탈퇴, REMOVED 강제 제외)';
COMMENT ON COLUMN schedule_space_members.joined_at IS '참여 시각';
COMMENT ON COLUMN schedule_space_members.created_at IS '생성 시각';
COMMENT ON COLUMN schedule_space_members.updated_at IS '수정 시각';