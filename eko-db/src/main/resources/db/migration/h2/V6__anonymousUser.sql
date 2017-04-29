INSERT INTO users (ID, VERSION, FIRST_NAME, LAST_NAME, MIDDLE_NAME, NOTE, ENABLED, LOGIN, PASSWORD)
VALUES (2, 0, 'anonymousUser', 'anonymousUser', 'anonymousUser', 'Anonymous user', TRUE, 'anonymousUser',
        '$2a$11$zc/nIAbq4qaJTEROpU7qeukL4ltKHFcIEkeb.n.fnLBlP.Uu.Ds7q');

INSERT INTO authorities (ID, VERSION, USER_ID, AUTHORITY)
VALUES (2, 0, 2, 'ADMIN')