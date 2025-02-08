--V2__add_words.sql

CREATE TABLE word
(
    word_id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Уникальный идентификатор слова
    text          VARCHAR(255) NOT NULL,                        -- Само слово
    transcription VARCHAR(255),                                 -- Транскрипция слова, если необходимо
    audio_url     VARCHAR(255),                                 -- Ссылка на аудио (если используется)
    image_url     VARCHAR(255)
);

CREATE TABLE word_translations
(
    word_translations_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- Уникальный идентификатор перевода
    word_id              INT          NOT NULL,                         -- Идентификатор слова
    language_code        VARCHAR(10)  NOT NULL,                         -- Код языка (например, 'en' для английского)
    translation          VARCHAR(255) NOT NULL,                         -- Перевод слова
    example_sentence     TEXT,                                          -- Пример использования
    image_url            VARCHAR(255),                                  -- Картинка для перевода
    FOREIGN KEY (word_id) REFERENCES word (word_id) ON DELETE CASCADE -- Связь со словом
);


CREATE TABLE learning_status_reference
(
    status_id   bigint primary key generated always as identity,
    percentage  int         NOT NULL UNIQUE,
    status_name varchar(50) NOT NULL
);

-- Добавляем справочные данные
INSERT INTO learning_status_reference (percentage, status_name)
VALUES (0, 'Not Started'),
       (25, 'Started'),
       (50, 'In Progress'),
       (75, 'Almost Completed'),
       (100, 'Completed');

CREATE TABLE learning_status
(

    learning_status_id bigint primary key generated always as identity,
    user_id            bigint not null references users(user_id),
    word_id            bigint NOT NULL REFERENCES word (word_id),
    status_id          bigint NOT NULL REFERENCES learning_status_reference (status_id)
);



CREATE TABLE user_words (
    user_words_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,   -- Уникальный идентификатор записи
    user_id INT NOT NULL,                                         -- Идентификатор пользователя
    word_id INT NOT NULL,                                         -- Идентификатор слова
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE, -- Связь с пользователем
    FOREIGN KEY (word_id) REFERENCES word (word_id) ON DELETE CASCADE  -- Связь со словом
);


CREATE TABLE word_app.task_status (
    task_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

