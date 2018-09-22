insert into audit_event_type (id, name, systemname, is_loggable)
values (0, 'Вход в систему', 'LOGIN', true);
insert into audit_event_type (id, name, systemname, is_loggable)
values (1, 'Выход из системы', 'LOGOUT', true);

insert into audit_event_attribute (id, name, systemname)
values (0, 'Имя пользователя', 'USER_NAME');