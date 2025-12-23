CREATE TABLE policy (
    policy_id UUID PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    fecha_inicio DATE NOT NULL,
    value NUMERIC(15,2) NOT NULL
);
