CREATE TABLE users (
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  version     INT,
  login       VARCHAR(256) UNIQUE NOT NULL,
  first_name  VARCHAR(256),
  last_name   VARCHAR(256),
  middle_name VARCHAR(256),
  note        VARCHAR(256),
  password    VARCHAR(256),
  enabled     BOOLEAN             NOT NULL
);

CREATE UNIQUE INDEX ix_users
  ON users (id);

CREATE TABLE AUTHORITIES (
  id             BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  version        INT,
  authority_name VARCHAR(256) NOT NULL UNIQUE
);

COMMENT ON TABLE AUTHORITIES IS 'Типы полномочий';
COMMENT ON COLUMN AUTHORITIES.id IS 'Идентификатор типа';
COMMENT ON COLUMN AUTHORITIES.VERSION IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN AUTHORITIES.authority_name IS 'Имя типа полномочий';

CREATE UNIQUE INDEX ix_authorities
  ON authorities (id);


CREATE TABLE user_authorities (
  id           BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  version      INT,
  user_id      BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  AUTHORITY_ID BIGINT NOT NULL REFERENCES AUTHORITIES (ID) ON DELETE CASCADE,
  CONSTRAINT fk_user_authorities_group FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_user_authorities FOREIGN KEY (AUTHORITY_ID) REFERENCES AUTHORITIES (id)
);

CREATE UNIQUE INDEX ix_auth_username
  ON user_authorities (user_id, authority_id);

COMMENT ON COLUMN USER_AUTHORITIES.AUTHORITY_ID IS 'Тип полномочий';

CREATE TABLE groups (
  id         BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  group_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE UNIQUE INDEX ix_groups
  ON groups (id);

CREATE UNIQUE INDEX ix_group_names
  ON groups (GROUP_NAME);

CREATE TABLE group_authorities (
  id           BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  group_id     BIGINT NOT NULL REFERENCES groups (id) ON DELETE CASCADE,
  AUTHORITY_ID BIGINT NOT NULL REFERENCES AUTHORITIES (ID) ON DELETE CASCADE,
  CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id) REFERENCES groups (id),
  CONSTRAINT fk_group_authorities FOREIGN KEY (AUTHORITY_ID) REFERENCES AUTHORITIES (id)
);

CREATE UNIQUE INDEX ix_group_authorities
  ON group_authorities (id);

COMMENT ON COLUMN GROUP_AUTHORITIES.AUTHORITY_ID IS 'Тип полномочий';

CREATE TABLE group_members (
  id       BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  user_id  BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  group_id BIGINT NOT NULL REFERENCES groups (id) ON DELETE CASCADE,
  CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE UNIQUE INDEX ix_group_members
  ON group_members (id);

CREATE TABLE persistent_logins (
  id        BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  user_id   BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  series    VARCHAR(64),
  token     VARCHAR(64) NOT NULL,
  last_used TIMESTAMP   NOT NULL
);

CREATE UNIQUE INDEX ix_persistent_logins
  ON persistent_logins (id);
