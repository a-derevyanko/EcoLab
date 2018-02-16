CREATE TABLE lab2data (
  /**
  *Блок системных свойств
  */
  id                                        BIGINT             GENERATED BY DEFAULT AS IDENTITY ( START WITH 0) PRIMARY KEY,
  user_id                                   BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  start_date                                TIMESTAMP NOT NULL,
  save_date                                 TIMESTAMP NOT NULL,
  version                                   INT,
  completed                                 BOOLEAN   NOT NULL DEFAULT FALSE,

  /**
  *Блок свойств лабы
  */
  barometric_Pressure                  INT,
  indoors_Temperature                  INT,
  room_Size                            INT,
  quantity_Of_Single_Type_Equipment    INT,
  hemisphere_Radius                    DOUBLE,
  average_Sound_Pressure               ARRAY,
  correction_Factor                    DOUBLE,
  sound_Pressure_Measuring_Surface     DOUBLE,
  hemisphere_Surface                   DOUBLE,
  measuring_Factor                     DOUBLE,
  sound_Power_Level                    DOUBLE,
  room_Constant_1000                   DOUBLE,
  frequency_Coefficient                DOUBLE,
  room_Constant                        DOUBLE,
  reflected_Sound_Power                DOUBLE,

  /**
  * Блок ключей
  */
  CONSTRAINT fk_LAB2DATA_user FOREIGN KEY (user_id) REFERENCES users (id)
);

/*
* Создадим индексы
*/
CREATE UNIQUE INDEX ix_lab2data_id
  ON lab2data (id);
CREATE INDEX ix_LAB2DATA_user_id
  ON lab2data (user_id);
CREATE INDEX ix_LAB2DATA_user_id_start_date
  ON lab2data (USER_ID, START_DATE);

/*
* Добавим комментарии
*/
COMMENT ON TABLE LAB2DATA IS 'Данные лабораторной №2';
COMMENT ON COLUMN LAB2DATA.id IS 'Уникальный идентификатор выполненного варианта';
COMMENT ON COLUMN LAB2DATA.user_id IS 'ID пользователя, выполнившего задание';
COMMENT ON COLUMN LAB2DATA.start_date IS 'Дата и время начала выполнения лабораторной';
COMMENT ON COLUMN LAB2DATA.save_date IS 'Дата и время последнего сохранения результатов';
COMMENT ON COLUMN LAB2DATA.version IS 'Версия изменений(контроль одновременного доступа)';
COMMENT ON COLUMN LAB2DATA.completed IS 'Признак завершенности лабораторной работы';
COMMENT ON COLUMN LAB2DATA.barometric_Pressure IS 'Барометрическое давление';
COMMENT ON COLUMN LAB2DATA.indoors_Temperature IS 'Температура воздуха в помещении';
COMMENT ON COLUMN LAB2DATA.room_Size IS 'Объем помещения с исследуемым объектом';
COMMENT ON COLUMN LAB2DATA.quantity_Of_Single_Type_Equipment IS 'Количество однотипного оборудования';
COMMENT ON COLUMN LAB2DATA.hemisphere_Radius IS 'Радиус полусферы';
COMMENT ON COLUMN LAB2DATA.average_Sound_Pressure IS 'Средний уровень звукового давления';
COMMENT ON COLUMN LAB2DATA.correction_Factor IS 'Корректирующая поправка';
COMMENT ON COLUMN LAB2DATA.sound_Pressure_Measuring_Surface IS 'Уровень звукового давления на измерительной поверхности';
COMMENT ON COLUMN LAB2DATA.hemisphere_Surface IS 'Поверхность полусферы';
COMMENT ON COLUMN LAB2DATA.measuring_Factor IS 'Показатель измерительной поверхности';
COMMENT ON COLUMN LAB2DATA.sound_Power_Level IS 'Уровень звуковой мощности';
COMMENT ON COLUMN LAB2DATA.room_Constant_1000 IS 'Постоянная помещения на среднегеометрической частоте 1000 Гц';
COMMENT ON COLUMN LAB2DATA.room_Constant IS 'Постоянная помещения';
COMMENT ON COLUMN LAB2DATA.reflected_Sound_Power IS 'Уровни звукового давления в зоне отраженного звука';


CREATE TABLE LAB2_RANDOM_VARIANT (
  id                           BIGINT IDENTITY PRIMARY KEY REFERENCES LAB2DATA (id) ON DELETE CASCADE,
  version                      INT,
  /**
  *Блок свойств лабы
  */
  name                                 VARCHAR(256),
  barometric_Pressure                  INT,
  indoors_Temperature                  INT,
  quantity_Of_Single_Type_Equipment    INT,
  average_Sound_Pressure               ARRAY,
  estimated_Geometric_Mean_Frequency   DOUBLE
);

/*
* Создадим индексы
*/
CREATE UNIQUE INDEX ix_LAB2_RANDOM_VARIANT_id
  ON LAB2_RANDOM_VARIANT (id);

/*
* Добавим комментарии
*/
COMMENT ON TABLE LAB2_RANDOM_VARIANT IS 'Вариант лабораторной №2';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.name IS 'Название объекта';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.barometric_Pressure IS 'Барометрическое давление';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.indoors_Temperature IS 'Температура воздуха в помещении';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.quantity_Of_Single_Type_Equipment IS 'Количество однотипного оборудования';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.average_Sound_Pressure IS 'Средний уровень звукового давления';
COMMENT ON COLUMN LAB2_RANDOM_VARIANT.estimated_Geometric_Mean_Frequency IS 'Расчетная среднегеометрическая частота';

CREATE TABLE lab2_experiment_log (
  id                           BIGINT IDENTITY PRIMARY KEY REFERENCES LAB2DATA (id) ON DELETE CASCADE,
  version                      INT,
  /**
  *Блок свойств лабы
  */
  name                                 VARCHAR(256),
  barometric_Pressure                  INT,
  indoors_Temperature                  INT,
  room_Size                            INT,
  quantity_Of_Single_Type_Equipment    INT,
  hemisphere_Radius                    DOUBLE,
  average_Sound_Pressure               ARRAY,
  estimated_Geometric_Mean_Frequency   DOUBLE
);

/*
* Создадим индексы
*/
CREATE UNIQUE INDEX ix_lab2_experiment_log_id
  ON lab2_experiment_log (id);

/*
* Добавим комментарии
*/
COMMENT ON TABLE lab2_experiment_log IS 'Вариант лабораторной №2';
COMMENT ON COLUMN lab2_experiment_log.name IS 'Название объекта';
COMMENT ON COLUMN lab2_experiment_log.barometric_Pressure IS 'Барометрическое давление';
COMMENT ON COLUMN lab2_experiment_log.indoors_Temperature IS 'Температура воздуха в помещении';
COMMENT ON COLUMN lab2_experiment_log.room_Size IS 'Объем помещения с исследуемым объектом';
COMMENT ON COLUMN lab2_experiment_log.quantity_Of_Single_Type_Equipment IS 'Количество однотипного оборудования';
COMMENT ON COLUMN lab2_experiment_log.hemisphere_Radius IS 'Радиус полусферы';
COMMENT ON COLUMN lab2_experiment_log.average_Sound_Pressure IS 'Средний уровень звукового давления';
COMMENT ON COLUMN lab2_experiment_log.estimated_Geometric_Mean_Frequency IS 'Расчетная среднегеометрическая частота';


CREATE TABLE lab2_experiment_log_sound_pressure (
  id      BIGINT IDENTITY PRIMARY KEY,
  LAB_ID  BIGINT REFERENCES lab2_experiment_log (id) ON DELETE CASCADE,
  version INT,
  F_315   DOUBLE,
  F_63    DOUBLE,
  F_125   DOUBLE,
  F_250   DOUBLE,
  F_500   DOUBLE,
  F_1000  DOUBLE,
  F_2000  DOUBLE,
  F_4000  DOUBLE,
  F_8000  DOUBLE
);

COMMENT ON TABLE lab2_experiment_log_sound_pressure IS 'Звуковое давление для режима эксперимента лабораторной №2';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.id is 'Идентификатор';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.LAB_ID is 'Идентификатор лабораторной';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_315 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_63 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_125 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_250 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_500 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_1000 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_2000 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_4000 is 'Давление';
COMMENT ON COLUMN lab2_experiment_log_sound_pressure.F_8000 is 'Давление';