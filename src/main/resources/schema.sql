create table if not exists acl_sid
(
    id        bigserial    not null primary key,
    principal boolean      not null,
    sid       varchar(100) not null,
    constraint unique_uk_1 unique (sid, principal)
);

create table if not exists acl_class
(
    id    bigserial    not null primary key,
    class varchar(100) not null,
    constraint unique_uk_2 unique (class)
);

create table if not exists acl_object_identity
(
    id                 bigserial primary key,
    object_id_class    bigint      not null,
    object_id_identity varchar(36) not null,
    parent_object      bigint,
    owner_sid          bigint,
    entries_inheriting boolean     not null,
    constraint unique_uk_3 unique (object_id_class, object_id_identity),
    constraint foreign_fk_1 foreign key (parent_object) references acl_object_identity (id),
    constraint foreign_fk_2 foreign key (object_id_class) references acl_class (id),
    constraint foreign_fk_3 foreign key (owner_sid) references acl_sid (id)
);


create table if not exists acl_entry
(
    id                  bigserial primary key,
    acl_object_identity bigint  not null,
    ace_order           int     not null,
    sid                 bigint  not null,
    mask                integer not null,
    granting            boolean not null,
    audit_success       boolean not null,
    audit_failure       boolean not null,
    constraint unique_uk_4 unique (acl_object_identity, ace_order),
    constraint foreign_fk_4 foreign key (acl_object_identity) references acl_object_identity (id),
    constraint foreign_fk_5 foreign key (sid) references acl_sid (id)
);

create table if not exists roles
(
    id   bigint       not null primary key,
    name varchar(100) not null,
    constraint unique_uk_5 unique (name)
);

create table if not exists groups
(
    id          bigserial primary key,
    name        varchar(100) not null,
    description varchar(255) not null,
    constraint unique_uk_6 unique (name)
);

create table if not exists groups_members
(
    id       bigserial primary key,
    user_id  bigint not null,
    group_id bigint not null,
    constraint unique_uk_7 unique (user_id, group_id),
    constraint foreign_fk_7 foreign key (group_id) references groups (id),
    constraint foreign_fk_8 foreign key (user_id) references users (id)
);

BEGIN;
INSERT INTO public.roles (id, name)
SELECT role.id, role.name
FROM (
         SELECT 1            as id,
                'ROLE_ADMIN' as name
         UNION
         SELECT 2              as id,
                'ROLE_MANAGER' as name
         UNION
         SELECT 3               as id,
                'ROLE_REGISTER' as name
         UNION
         SELECT 4           as id,
                'ROLE_USER' as name
         UNION
         SELECT 5            as id,
                'ROLE_GUEST' as name
     ) as role
WHERE (SELECT COUNT(*) FROM roles) <= 0;
COMMIT;
END;