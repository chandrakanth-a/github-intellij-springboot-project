-- PostgreSQL schema for Public API Gateway App – Backend

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(100) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS api_endpoints (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_api_endpoints_owner ON api_endpoints(owner_id);

CREATE TABLE IF NOT EXISTS api_fetch_results (
    id UUID PRIMARY KEY,
    endpoint_id UUID NOT NULL REFERENCES api_endpoints(id) ON DELETE CASCADE,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status_code INT,
    success BOOLEAN,
    content_type VARCHAR(255),
    response_body TEXT,
    duration_ms BIGINT,
    fetched_at TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_results_owner ON api_fetch_results(owner_id);
CREATE INDEX IF NOT EXISTS idx_results_endpoint ON api_fetch_results(endpoint_id);
