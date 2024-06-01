create table if not exists Currencies (
    id integer primary key autoincrement,
    code text check (length(code) == 3) not null,
    full_name text not null,
    sign text not null
);

create table if not exists ExchangeRates (
    id integer primary key autoincrement ,
    base_currency_id integer not null,
    target_currency_id integer not null,
    rate real not null,
    foreign key (base_currency_id) references Currencies (id),
    foreign key (target_currency_id) references Currencies (id),
    constraint exchange_rates_unique_currency_id unique
    (base_currency_id, target_currency_id)
);