create table customers (
    id serial primary key,
    created_at timestamp,
    name varchar(255),
    email varchar(255),
    phone_number varchar(255),
    unique (phone_number)
);

create type offer_types as enum ("postpaid", "prepaid");

create table offers (
    id serial primary key,
    created_at timestamp,
    name varchar(100),
    duration int,
    amount decimal,
    cons_interior decimal,
    cons_exterior decimal,
    price_prepaid decimal,
    price_postpaid decimal,
    limit_per_period int,
    limit_period int,
    offer_type offer_types
);

create table offer_types (
    id serial primary key,
    name varchar(10)
);

create table offer_types_offer (
    id serial primary key,
    offer_id int,
    offer_type_id int,
    foreign key (offer_id) references offers(id),
    foreign key (offer_type_id) references offer_types(id)
);

create table offer_parents (
    id serial primary key,
    offer_id int,
    offer_parent_id int,
    foreign key (offer_id) references offers(id),
    foreign key (offer_parent_id) references offers(id)
);

create table customers_subscriptions (
    id serial primary key,
    created_at timestamp,
    customer_id int,
    foreign key (customer_id) references customers(id)
);

create table customers_payments (
    id serial primary key,
    created_at timestamp,
    customers_subscription_id int,
    foreign key (customers_subscription_id) references customers_subscriptions(id)
);

create table fees (
    id serial primary key,
    created_at timestamp,
    amount_min decimal,
    amount_max decimal,
    amount_fee decimal
);

create table deposits (
    id serial primary key,
    created_at timestamp,
    customer_id int,
    amount decimal
);

create table withdraws (
    id serial primary key,
    created_at timestamp,
    customer_id int,
    amount decimal,
    fee decimal
);




/* DATA EXAMPLE */
create view deposits_sums as select customer_id, sum(amount) as sum_deposits from deposits group by customer_id;
create view withdraws_sums as select customer_id sum(amount - fee) as sum_withdraws from withdraws group by customer_id;
create view customers_balances as select customers.id, customer_id, (coalesce(sum_deposits, 0) - coalesce(sum_withdraws, 0)) as balance from customers left join deposits_sums on deposits_sums.customer_id = customers.id left join withdraws_sums on withdraws_sums.customer_id = customers.id;



insert into customers (created_at, name, email, phone_number) values ('2021-03-11 15:00', 'razanakoto pascal', 'rakoto@gmail.com', '0321215222');

insert into offers (created_at, name, duration, amount, price_postpaid, limit_per_period, limit_period, offer_type) values ('2020-01-01 00:00', 'net 10 Go', 30, -1, 'postpaid');