/*create database db_operateur;
create role operateur login password '123456';
alter database db_operateur owner to operateur;
*/
drop schema mg cascade;

create schema mg;

create table mg.conf_operator (
    id serial primary key,
    created_at timestamp not null,
    prefix varchar(6),
    name varchar(25)
);

create table mg.users (
    id serial primary key,
    created_at timestamp not null,
    name varchar(255) not null,
    pwd varchar(32) not null,
    unique(name)
);

create table mg.customers (
    id serial primary key,
    created_at timestamp not null,
    name varchar(255) not null,
    email varchar(25),
    phone_number char(13) not null,
    password VARCHAR(32) not null,
    unique (phone_number),
    unique (email)
);


create table mg.fees (
    id serial primary key,
    created_at timestamp not null,
    amount_min decimal check(amount_min >= 0),
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
    isValidated boolean default false,
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


/* TODO: insert offer purchase into this table */
create table mg.offer_purchases (
    id serial primary key,
    customer_id int not null,
    date date not null,
    offer_id int not null,
    foreign key (customer_id) references mg.customers (id)
);

create sequence mg.offerSeq;
create sequence mg.purhaseSeq;

create table mg.messages_calls_consumptions (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    customer_destination_id int,
    amount int  check (amount > 0),
    t_type char(1) not null,
    foreign key (customer_id) references mg.customers(id),
    foreign key (customer_destination_id) references mg.customers(id)
);



/*TODO: Store message to MONGODB */
create table mg.messages_pricings (
    id serial primary key,
    created_at timestamp not null,
    amount_interior decimal check(amount_interior > 0),
    amount_exterior decimal check(amount_exterior > 0),
    unit int default 160
);

create table mg.calls_pricings (
    id serial primary key,
    created_at timestamp not null,
    amount_interior decimal check(amount_interior > 0),
    amount_exterior decimal check(amount_exterior > 0)
);

create table mg.internet_pricings (
    id serial primary key,
    created_at timestamp not null,
    amount decimal check(amount > 0)
);

create table mg.internet_applications (
    id serial primary key,
    name varchar(255) not null,
    created_at timestamp not null
);

create table mg.internet_consumptions (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    amount decimal check(amount > 0),
    internet_application_id int,
    foreign key (customer_id) references mg.customers(id),
    foreign key (internet_application_id) references mg.internet_applications(id)
);
/* STATS */
create view mg.deposits_stat as select date(created_at), sum(amount) as amount from mg.deposits where isValidated is true group by date(created_at) order by date(created_at);
create view mg.withdraws_stat as select date(created_at), sum(amount) as amount from mg.withdraws group by date(created_at) order by date(created_at) ;
    
/* CALLS history */
create view mg.calls_history as select mg.messages_calls_consumptions.created_at, mg.messages_calls_consumptions.amount, mg.customers.phone_number, 
mg.messages_calls_consumptions.customer_id 
from mg.messages_calls_consumptions join
mg.customers on mg.customers.id = mg.messages_calls_consumptions.customer_destination_id where mg.messages_calls_consumptions.t_type = 'c';

/* ALL NON VALIDATED DEPOSITS */
create view mg.all_customers_deposits as select mg.deposits.*, mg.customers.phone_number, mg.customers.name from mg.deposits join mg.customers 
on mg.customers.id = mg.deposits.customer_id;

/* MAX DATE OPERATION */
create view mg.all_customer_operations as 
(select created_at, customer_id from mg.deposits union all 
select created_at, customer_id from mg.withdraws union all
select created_at, customer_id from mg.buyed_credits union all
select created_at, customer_id from mg.credit_consumptions union all
select date, customer_id from mg.offer_purchases union all
select created_at, customer_id from mg.messages_calls_consumptions union all
select created_at, customer_id from mg.internet_consumptions union all
select created_at, id from mg.customers
);

/* MOBILE MONEY */
create view mg.deposits_sums as select customer_id, sum(amount) as sum_deposits from mg.deposits where isValidated = true group by customer_id;
create view mg.withdraws_sums as select customer_id, sum(amount + fee) as sum_withdraws from mg.withdraws group by customer_id;
create view mg.customers_balances as select mg.customers.id, (coalesce(sum_deposits, 0) - coalesce(sum_withdraws, 0)) as balance 
from mg.customers left join mg.deposits_sums on mg.deposits_sums.customer_id = mg.customers.id 
left join mg.withdraws_sums on mg.withdraws_sums.customer_id = mg.customers.id;


/* CREDIT */
create view mg.buyed_credit_sums as select customer_id, sum(amount) as sum_buyed_credit from mg.buyed_credits group by customer_id;
create view mg.credit_consumption_sums as select customer_id, sum(cons_amount) as sum_cons_amount from mg.credit_consumptions group by customer_id;
create view mg.customers_credit_balances as select mg.customers.id, (coalesce(sum_buyed_credit,0) - coalesce(sum_cons_amount,0)) as balance from mg.customers left join mg.buyed_credit_sums on mg.buyed_credit_sums.customer_id = mg.customers.id left join mg.credit_consumption_sums on mg.credit_consumption_sums.customer_id = mg.customers.id;
insert into mg.customers (created_at, name, email, phone_number, password) values  ('2000-01-01', 'exterior user', 'exterior@gmail.com', '0', '63a9f0ea7bb98050796b649e85481845');
insert into mg.conf_operator (created_at, prefix , name) values ('2000-01-01', '+26133', 'kabs');


insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 100, 1000, 50);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 1001, 5000, 50);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 5001, 10000, 100);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 10001, 25000, 200);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 25001, 50000, 400);
insert into mg.fees (created_at, amount_min, amount_max, amount_fee) values ('2000-01-01 00:00', 50001, 100000, 800);

insert into mg.internet_applications (name, created_at) values ('Facebook', '2021-03-19');
insert into mg.internet_applications (name, created_at) values ('Instagram', '2021-03-19');
insert into mg.internet_applications (name, created_at) values ('Tiktok', '2021-03-19');
insert into mg.internet_applications (name, created_at) values ('Internet', '2021-03-19');
insert into mg.messages_pricings (created_at, amount_interior, amount_exterior, unit) values ('2000-01-01', 100,200, 10);
insert into mg.calls_pricings (created_at, amount_interior, amount_exterior) values ('2000-01-01', 2,2);
insert into mg.internet_pricings (created_at, amount) values ('2000-01-01', 2);

/*
insert into mg.users (created_at, name, pwd) values ('2000-01-01 00:00', 'admin', '63a9f0ea7bb98050796b649e85481845');




*/
select * from mg.deposits;
select * from mg.withdraws;
select * from mg.customers_balances order by id;
select * from mg.customers_credit_balances order by id;
select * from mg.buyed_credits;
select * from mg.customers;