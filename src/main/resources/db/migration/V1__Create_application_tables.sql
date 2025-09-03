CREATE TABLE applications (
    application_id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    selected_plan VARCHAR(50) NOT NULL,
    selected_device VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    current_step INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE application_steps (
    step_id BIGSERIAL PRIMARY KEY,
    application_id VARCHAR(50) NOT NULL,
    step_number INTEGER NOT NULL,
    step_data TEXT,
    completed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(application_id) ON DELETE CASCADE
);

CREATE INDEX idx_applications_email ON applications(email);
CREATE INDEX idx_application_steps_application_id ON application_steps(application_id);
CREATE INDEX idx_application_steps_step_number ON application_steps(application_id, step_number);
