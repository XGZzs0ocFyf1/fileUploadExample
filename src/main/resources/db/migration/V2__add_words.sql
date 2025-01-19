--V2__add_words.sql
CREATE TABLE word_app.language
(
    language_id bigint primary key generated always as identity,
    code        varchar(10) NOT NULL UNIQUE,
    name        varchar(100) NOT NULL
);

CREATE TABLE word_app.word
(
    word_id       bigint primary key generated always as identity,
    original_text varchar(255) NOT NULL,
    translation   varchar(255) NOT NULL,
    language_id   bigint NOT NULL,
    FOREIGN KEY (language_id) REFERENCES word_app.language (language_id)
);

CREATE TABLE word_app.image
(
    image_id    bigint primary key generated always as identity,
    file_name   varchar(255) NOT NULL,
    description text,
    word_id     bigint NOT NULL,
    FOREIGN KEY (word_id) REFERENCES word_app.word (word_id) ON DELETE CASCADE
);


CREATE TABLE word_app.settings
(
    settings_id        bigint primary key generated always as identity,
    user_id            bigint NOT NULL UNIQUE,
    interface_settings text,
    preferred_language bigint NOT NULL,
    FOREIGN KEY (preferred_language) REFERENCES word_app.language (language_id),
    FOREIGN KEY (user_id) REFERENCES word_app.users (user_id)
);

CREATE TABLE word_app.learning_status_reference
(
    status_id bigint primary key generated always as identity,
    percentage int NOT NULL UNIQUE,
    status_name varchar(50) NOT NULL
);

-- Добавляем справочные данные
INSERT INTO word_app.learning_status_reference (percentage, status_name)
VALUES
    (0, 'Not Started'),
    (25, 'Started'),
    (50, 'In Progress'),
    (75, 'Almost Completed'),
    (100, 'Completed');

CREATE TABLE word_app.learning_status
(
    learning_status_id bigint primary key generated always as identity,
    word_id            bigint NOT NULL,
    status_id          bigint NOT NULL,
    FOREIGN KEY (word_id) REFERENCES word_app.word (word_id),
    FOREIGN KEY (status_id) REFERENCES word_app.learning_status_reference (status_id)
);
