insert into audit_event_type (id, name, systemname, is_loggable)
values (2, 'Отобран допуск к лабораторной', 'LAB_DISALLOWED', true);

insert into audit_event_type (id, name, systemname, is_loggable)
values (3, 'Дан допуск к лабораторной', 'LAB_ALLOWED', true);

insert into audit_event_type (id, name, systemname, is_loggable)
values (4, 'Отобран допуск к защите лабораторной', 'LAB_DEFENCE_DISALLOWED', true);

insert into audit_event_type (id, name, systemname, is_loggable)
values (5, 'Дан допуск к защите лабораторной', 'LAB_DEFENCE_ALLOWED', true);

insert into audit_event_attribute (id, name, systemname)
values (1, 'Пользователь, к которому относится событие', 'CONSUMER_NAME');

insert into audit_event_attribute (id, name, systemname)
values (2, 'Номер лабораторной', 'LAB_NUMBER');