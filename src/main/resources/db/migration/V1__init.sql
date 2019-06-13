/* Schemas & Roles Setup  */
DO
$body$
BEGIN
    IF NOT EXISTS (
        SELECT *
        FROM pg_catalog.pg_user
        WHERE usename = 'service') THEN

        CREATE ROLE service LOGIN;
    END IF;
END
$body$;

GRANT USAGE ON SCHEMA phone_numbers TO service;
ALTER DEFAULT PRIVILEGES IN SCHEMA phone_numbers GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO service;

/* Table creation */
CREATE TABLE file_process (
    id                  VARCHAR PRIMARY KEY,
    original_filename   VARCHAR NOT NULL,
    valid_count         INTEGER,
    fixed_count         INTEGER,
    invalid_count       INTEGER,
    created_count       INTEGER,
    updated_count       INTEGER,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

/* Table creation */
CREATE TABLE process_detail (
    id                  VARCHAR PRIMARY KEY,
    file_process_id     VARCHAR NOT NULL,
    original_value      VARCHAR NOT NULL,
    processed_value     VARCHAR,
    status              VARCHAR NOT NULL,
    reason              VARCHAR,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE process_detail ADD CONSTRAINT fk_process_detail_file_id FOREIGN KEY (file_process_id) REFERENCES file_process(id);