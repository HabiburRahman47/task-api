create table tasks (
    id bigserial primary key,
    title varchar(200) not null,
    description text,
    status varchar(30) not null,
    created_at timestamp with time zone not null
);