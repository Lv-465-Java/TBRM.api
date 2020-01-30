INSERT INTO public.roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_MANAGER'),
       (3, 'ROLE_REGISTER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_GUEST');

INSERT INTO public.acl_sid(id, principal, sid)
VALUES (1, false, 'MANAGER');