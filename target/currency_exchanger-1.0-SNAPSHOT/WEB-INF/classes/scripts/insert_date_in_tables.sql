insert into Currencies (code, full_name, sign)
values ('USD', 'US Dollar', '$'),
     ('EUR', 'Euro', '€'),
     ('RUB', 'Russian Ruble', '₽'),
     ('GBP', 'Pound Sterling', '£'),
     ('UAH', 'Hryvnia', '₴'),
     ('KZT', 'Tenge', '₸');

insert into ExchangeRates (base_currency_id, target_currency_id, rate)
values (1, 2,0.94),
        (1, 3, 63.75),
        (1, 4, 36.95),
        (1, 5, 469.88),
        (1, 6, 0.81);
