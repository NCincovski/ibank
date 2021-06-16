drop table if exists customers;
create table customers
(
    id   bigint,
    name varchar(100) not null,
    constraint customers_pkey primary key (id)
);

drop table if exists accounts;
create table accounts
(
    id              bigint,
    account_number  char(36) not null,
    current_balance decimal default 0,
    customer_id     bigint   not null,
    constraint accounts_pkey primary key (id),
    constraint customers_fkey foreign key (customer_id) references customers (id),
    constraint unique_account_number unique (account_number)
);

drop table if exists transactions;
create table transactions
(
    id           bigint,
    amount       decimal   not null,
    from_account char(36)  not null,
    to_account   char(36)  not null,
    time         timestamp not null,
    constraint transactions_pkey primary key (id)
);