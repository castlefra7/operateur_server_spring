/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  dodaa
 * Created: 18 mars 2021
 */

create table mg.offer_purchases (
    id serial primary key,
    customer_id int not null,
    date date not null,
    offer_id int not null,
    foreign key (customer_id) references mg.customers (id)
);

create sequence mg.offerSeq;
