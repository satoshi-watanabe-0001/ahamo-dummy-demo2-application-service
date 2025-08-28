CREATE TABLE application_steps (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    step_number INTEGER NOT NULL,
    step_name VARCHAR(255) NOT NULL,
    step_data TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    validation_errors TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
);

CREATE INDEX idx_application_steps_application_id ON application_steps(application_id);
CREATE INDEX idx_application_steps_step_number ON application_steps(step_number);
CREATE INDEX idx_application_steps_status ON application_steps(status);
CREATE UNIQUE INDEX idx_application_steps_unique ON application_steps(application_id, step_number);

COMMENT ON TABLE application_steps IS '申し込みステップ情報を管理するテーブル';
COMMENT ON COLUMN application_steps.application_id IS '申し込みID（applicationsテーブルの外部キー）';
COMMENT ON COLUMN application_steps.step_number IS 'ステップ番号（1-5）';
COMMENT ON COLUMN application_steps.step_name IS 'ステップ名';
COMMENT ON COLUMN application_steps.step_data IS 'ステップデータ（JSON形式）';
COMMENT ON COLUMN application_steps.status IS 'ステップ状況（PENDING, IN_PROGRESS, COMPLETED, SKIPPED, ERROR）';
COMMENT ON COLUMN application_steps.validation_errors IS 'バリデーションエラー情報';
COMMENT ON COLUMN application_steps.created_at IS '作成日時';
COMMENT ON COLUMN application_steps.updated_at IS '更新日時';
COMMENT ON COLUMN application_steps.completed_at IS '完了日時';
