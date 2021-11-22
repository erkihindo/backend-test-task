create table transaction
(
    id               UUID        not null,
    source_type      varchar(32) not null,
    source_id        UUID        not null,
    destination_type varchar(32) not null,
    destination_id   UUID        not null,
    state            varchar(32) not null,
    create_time      timestamp   not null,
    last_modified    timestamp   not null,
    primary key (id)
);
