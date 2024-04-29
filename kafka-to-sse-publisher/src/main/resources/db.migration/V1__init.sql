create table if not exists stocks (
    id uuid primary key,
    symbol varchar(10) not null,
    exchange varchar(50) not null,
    currency varchar(10) not null,
    price double precision not null,
    day_high_price double precision not null,
    day_low_price double precision not null,
    previous_close_price double precision not null,
    volume_traded bigint not null,
    trade_time timestamp not null
);