INSERT INTO users (ID, VERSION, FIRST_NAME, LAST_NAME, MIDDLE_NAME, NOTE, ENABLED, LOGIN, PASSWORD)
VALUES (1, 0, 'admin', 'admin', 'admin', 'Admin user', TRUE, 'admin',
        '$2a$11$zc/nIAbq4qaJTEROpU7qeukL4ltKHFcIEkeb.n.fnLBlP.Uu.Ds7q');

INSERT INTO authorities (ID, VERSION, USER_ID, AUTHORITY)
VALUES (1, 0, 1, 'ADMIN')