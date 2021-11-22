create table currency_account
(
    id            UUID        not null,
    account_id    UUID        not null,
    currency      varchar(32) not null,
    amount        varchar(30) not null,
    is_active     boolean     not null,
    create_time   timestamp   not null,
    last_modified timestamp   not null,
    primary key (id)
);
