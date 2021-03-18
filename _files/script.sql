/*create database db_operateur;
create role operateur login password '123456';
alter database db_operateur owner to operateur;
*/
drop schema mg cascade;

create schema mg;
create table mg.customers (
    id serial primary key,
    created_at timestamp not null,
    name varchar(255) not null,
    email varchar(255),
    phone_number char(13) not null,
    password VARCHAR(255),
    unique (phone_number)
);

create table mg.fees (
    id serial primary key,
    created_at timestamp not null,
    amount_min decimal,
    amount_max decimal check (amount_max > 0),
    amount_fee decimal check (amount_fee >= 0),
    unique (amount_min)
);

create table mg.deposits (
    id serial primary key,
    created_at timestamp,
    customer_id int,
    customer_source_id int,
    amount decimal check (amount > 0),
    foreign key (customer_id) references mg.customers(id),
    foreign key (customer_source_id) references mg.customers(id)
);

create table mg.withdraws (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    amount decimal check(amount > 0),
    fee decimal check (fee >= 0),
    foreign key (customer_id) references mg.customers(id)
);

create table mg.buyed_credits (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    customer_source_id int,
    amount decimal check(amount > 0),
    foreign key (customer_id) references mg.customers(id), 
    foreign key (customer_source_id) references mg.customers(id)
);

create table mg.credit_consumptions (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    cons_amount decimal check(cons_amount > 0),
    foreign key (customer_id) references mg.customers(id)
);

create table mg.offer_purchases (
    id serial primary key,
    customer_id int not null,
    date date not null,
    offer_id int not null,
    foreign key (customer_id) references mg.customers (id)
);

create table mg.applications (
    id serial primary key,
    name varchar(255)
);

create table mg.pricings (
    id serial primary key,
    created_at date not null,
    application_id int,
    amount_interior decimal check(amount_interior > 0),
    amount_exterior decimal check(amount_exterior > 0),
    foreign key (application_id) references mg.applications(id),
    unique (application_id)
);


-- create table mg.consumptions (
--     id serial primary key,

-- )

/* MAX DATE OPERATION */
create view mg.all_customer_operations as (select mg.deposits.created_at, mg.deposits.customer_id from mg.deposits union all select mg.withdraws.created_at, mg.withdraws.customer_id from mg.withdraws);

/* MOBILE MONEY */
create view mg.deposits_sums as select customer_id, sum(amount) as sum_deposits from mg.deposits group by customer_id;
create view mg.withdraws_sums as select customer_id, sum(amount + fee) as sum_withdraws from mg.withdraws group by customer_id;
create view mg.customers_balances as select mg.customers.id, (coalesce(sum_deposits, 0) - coalesce(sum_withdraws, 0)) as balance from mg.customers left join mg.deposits_sums on mg.deposits_sums.customer_id = mg.customers.id left join mg.withdraws_sums on mg.withdraws_sums.customer_id = mg.customers.id;


/* CREDIT */
create view mg.buyed_credit_sums as select customer_id, sum(amount) as sum_buyed_credit from mg.buyed_credits group by customer_id;
create view mg.credit_consumption_sums as select customer_id, sum(cons_amount) as sum_cons_amount from mg.credit_consumptions group by customer_id;
create view mg.customers_credit_balances as select mg.customers.id, (coalesce(sum_buyed_credit,0) - coalesce(sum_cons_amount,0)) as balance from mg.customers left join mg.buyed_credit_sums on mg.buyed_credit_sums.customer_id = mg.customers.id left join mg.credit_consumption_sums on mg.credit_consumption_sums.customer_id = mg.customers.id;

/* LAST OPERATION */
select max(((select created_at from mg.deposits where customer_id = customer_source_id and customer_id = 2  order by created_at desc limit 1) union (select created_at from mg.withdraws where customer_id = 2 order by created_at desc limit 1)));

/* DATA */
insert into mg.customers (created_at, name, email, phone_number, password) values ('2021-03-16 08:00', 'rakoto manou', 'rak@gmail.com', '+261331125636', '2811');
insert into mg.customers (created_at, name, email, phone_number, password) values ('2021-03-16 08:00', 'razaka rivo', 'rivo@gmail.com', '+261331525636', '0108');
insert into mg.customers (created_at, name, email, phone_number, password) values ('2021-03-16 08:00', 'ramanajaka rabe', 'rabe@gmail.com', '+261335125636', '2202');

insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 100, 1000, 50);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 1001, 5000, 50);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 5001, 10000, 100);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 10001, 25000, 200);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 25001, 50000, 400);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 50001, 100000, 800);

insert into mg.applications (name) values ('internet');
insert into mg.applications (name) values ('message');
insert into mg.applications (name) values ('call');

select * from mg.deposits;
select * from mg.withdraws;
select * from mg.customers_balances order by id;
select * from mg.customers_credit_balances order by id;
select * from mg.buyed_credits;