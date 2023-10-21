CREATE TABLE IF NOT EXISTS stat (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app varchar not null,
    uri varchar not null,
    ip varchar not null,
    click_time TIMESTAMP WITHOUT TIME ZONE not null
);
