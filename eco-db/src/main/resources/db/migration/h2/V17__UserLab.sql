
CREATE view user_lab_history as
  select 1 as lab_number, user_id, SAVE_DATE from lab1data where COMPLETED = 1
  union all
  select 2 as lab_number, user_id, SAVE_DATE from lab2data where COMPLETED = 1
  union all
  select 3 as lab_number, user_id, SAVE_DATE from lab3data where COMPLETED = 1;

COMMENT ON TABLE user_lab_history IS 'Выполненные пользователями лабораторные';
COMMENT ON COLUMN user_lab_history.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN user_lab_history.lab_number IS 'Номер лабораторной';
COMMENT ON COLUMN user_lab_history.SAVE_DATE IS 'Дата выполнения';
