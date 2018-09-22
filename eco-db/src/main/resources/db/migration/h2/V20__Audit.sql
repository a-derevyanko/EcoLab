
CREATE table audit_event_attribute  (
  id BIGINT PRIMARY KEY,
  version         INT,
  name VARCHAR(256) NOT NULL UNIQUE,
  systemname VARCHAR(256) NOT NULL UNIQUE
);

COMMENT ON TABLE audit_event_attribute IS 'Атрибуты событий аудита';
COMMENT ON COLUMN audit_event_attribute.id IS 'Идентификатор';
COMMENT ON COLUMN audit_event_attribute.version IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN audit_event_attribute.name IS 'Название атрибута';
COMMENT ON COLUMN audit_event_attribute.systemname IS 'Системное имя атрибута';

CREATE table audit_event_type  (
  id BIGINT PRIMARY KEY,
  version         INT,
  name VARCHAR(256) NOT NULL UNIQUE,
  is_loggable bool not null default false,
  systemname VARCHAR(256) NOT NULL UNIQUE
);

COMMENT ON TABLE audit_event_type IS 'Типы событий аудита';
COMMENT ON COLUMN audit_event_type.id IS 'Идентификатор';
COMMENT ON COLUMN audit_event_type.version IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN audit_event_type.name IS 'Тип события';
COMMENT ON COLUMN audit_event_type.is_loggable IS 'Признак того, что событие логгируется';
COMMENT ON COLUMN audit_event_type.systemname IS 'Системное имя события';

CREATE table audit  (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  version         INT,
  create_date datetime,
  event_type BIGINT NOT NULL REFERENCES audit_event_type (id) ON DELETE CASCADE,
  user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
);

COMMENT ON TABLE audit IS 'События аудита';
COMMENT ON COLUMN audit.id IS 'Идентификатор';
COMMENT ON COLUMN audit.version IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN audit.create_date IS 'Дата и время события';
COMMENT ON COLUMN audit.event_type IS 'Тип события';
COMMENT ON COLUMN audit.user_id IS 'Инициатор события';

CREATE table audit_attributes  (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  version         INT,
  audit_attribute_id BIGINT NOT NULL REFERENCES audit_event_attribute (id) ON DELETE CASCADE,
  audit_id BIGINT NOT NULL REFERENCES audit (id) ON DELETE CASCADE,
  value VARCHAR(256) NOT NULL ,
);

COMMENT ON TABLE audit_attributes IS 'Атрибуты события аудита';
COMMENT ON COLUMN audit_attributes.id IS 'Идентификатор';
COMMENT ON COLUMN audit_attributes.version IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN audit_attributes.audit_attribute_id IS 'Идентификатор типа атрибута';
COMMENT ON COLUMN audit_attributes.audit_id IS 'Идентификатор события';
COMMENT ON COLUMN audit_attributes.value IS 'Значение';


