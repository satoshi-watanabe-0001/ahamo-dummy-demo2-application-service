CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY,
    application_id VARCHAR(255) UNIQUE NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    selected_plan VARCHAR(255),
    selected_device VARCHAR(255),
    selected_options TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    current_step INTEGER NOT NULL DEFAULT 1,
    total_steps INTEGER NOT NULL DEFAULT 5,
    estimated_completion_time VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP
);

CREATE INDEX idx_applications_application_id ON applications(application_id);
CREATE INDEX idx_applications_email ON applications(email);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_applications_created_at ON applications(created_at);

COMMENT ON TABLE applications IS 'ahamoサービス申し込み情報を管理するテーブル';
COMMENT ON COLUMN applications.application_id IS '申し込み識別ID（APP-xxxxxxxx形式）';
COMMENT ON COLUMN applications.user_name IS 'ユーザー名';
COMMENT ON COLUMN applications.email IS 'メールアドレス';
COMMENT ON COLUMN applications.phone IS '電話番号';
COMMENT ON COLUMN applications.selected_plan IS '選択された料金プラン';
COMMENT ON COLUMN applications.selected_device IS '選択された端末';
COMMENT ON COLUMN applications.selected_options IS '選択されたオプション（JSON形式）';
COMMENT ON COLUMN applications.status IS '申し込み状況（DRAFT, IN_PROGRESS, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, COMPLETED）';
COMMENT ON COLUMN applications.current_step IS '現在のステップ番号';
COMMENT ON COLUMN applications.total_steps IS '総ステップ数';
COMMENT ON COLUMN applications.estimated_completion_time IS '完了予定時間';
COMMENT ON COLUMN applications.created_at IS '作成日時';
COMMENT ON COLUMN applications.updated_at IS '更新日時';
COMMENT ON COLUMN applications.submitted_at IS '提出日時';
