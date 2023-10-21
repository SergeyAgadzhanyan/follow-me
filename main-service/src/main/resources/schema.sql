CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     varchar not null,
    email    varchar not null,
    is_admin int     not null,
    UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar not null,
    UNIQUE (name)
);
CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         varchar                     not null,
    category_id        bigint                      not null,
    created_on         TIMESTAMP WITHOUT TIME ZONE not null,
    description        varchar                     not null,
    event_date         TIMESTAMP WITHOUT TIME ZONE not null,
    user_id            bigint                      not null,
    lat                bigint                      not null,
    lon                bigint                      not null,
    paid               int                         not null,
    participant_limit  int,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation int                         not null,
    state_enum         varchar                     not null,
    title              varchar                     not null,

    CONSTRAINT FK_events_category_id FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS participation_requests
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created  TIMESTAMP WITHOUT TIME ZONE,
    event_id bigint  not null,
    user_id  bigint  not null,
    status   varchar not null
);
CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned int     not null,
    title  varchar not null
);
CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT not null references compilations (id),
    event_id       BIGINT not null references events (id),
    CONSTRAINT pk PRIMARY KEY (compilation_id, event_id)
);
CREATE TABLE IF NOT EXISTS comment
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id   bigint                      not null,
    user_id    bigint                      not null,
    created_on TIMESTAMP WITHOUT TIME ZONE not null,
    updated_on TIMESTAMP WITHOUT TIME ZONE not null,
    text       varchar                     not null
);

