create table mg.messages_calls_consumptions (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    customer_destination_id int,
    amount int,
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
    created_at timestamp not null
);

create table mg.internet_consumptions (
    id serial primary key,
    created_at timestamp not null,
    customer_id int,
    amount decimal,
    internet_application_id int,
    foreign key (customer_id) references mg.customers(id),
    foreign key (internet_application_id) references mg.internet_applications(id)
);

insert into mg.messages_pricings (created_at, amount_interior, amount_exterior, unit) values ('2000-01-01', 100,100, 10);
insert into mg.calls_pricings (created_at, amount_interior, amount_exterior) values ('2000-01-01', 2,2);