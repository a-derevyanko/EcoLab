INSERT INTO users (ID, VERSION, FIRST_NAME, LAST_NAME, MIDDLE_NAME, NOTE, ENABLED, LOGIN, PASSWORD)
VALUES (0, 0, 'admin', 'admin', 'admin', 'Admin user', TRUE, 'admin',
        '$2a$11$zc/nIAbq4qaJTEROpU7qeukL4ltKHFcIEkeb.n.fnLBlP.Uu.Ds7q');

INSERT INTO authorities (id, version, authority_name)
VALUES (0, 0, 'ADMIN'),
        (1, 0, 'TEACHER'),
        (2, 0, 'STUDENT');

INSERT INTO USER_AUTHORITIES (ID, VERSION, USER_ID, AUTHORITY_ID)
VALUES (0, 0, 0, 0);

INSERT INTO groups (GROUP_NAME)
VALUES ('ADMIN'),
        ('TEACHER'),
        ('STUDENT');

INSERT INTO group_authorities (GROUP_ID, AUTHORITY_ID)
VALUES (0, 0),
        (0, 1),
        (0, 2),
        (1, 1),
        (1, 2),
        (2, 2);

INSERT INTO GROUP_MEMBERS (USER_ID, GROUP_ID)
VALUES (0, 0);