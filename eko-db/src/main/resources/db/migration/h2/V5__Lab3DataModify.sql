ALTER TABLE LAB3DATA ADD completed BOOLEAN NOT NULL DEFAULT FALSE;
CREATE UNIQUE INDEX ix_lab3data_user_id_start_date ON LAB3DATA(USER_ID,START_DATE);
COMMENT ON COLUMN LAB3DATA.COMPLETED IS 'Параметр завершенности лабораторной работы true - ЛР завершена. По умолчанию false'