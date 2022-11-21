begin transaction;
create table app_user
(
    id           int identity primary key,
    name         varchar(50) not null,
    last_name    varchar(50) not null,
    email        varchar(50) not null,
    phone_number varchar(15)
);

create table role
(
    id        int identity primary key,
    role_name varchar(50) not null,
)

create table user_role
(
    id      int identity primary key,
    user_id int not null references app_user (id),
    role_id int not null references role (id)
)

create table consultation
(
    id            int identity primary key,
    price         float default 0 not null,
    user_details  varchar(1000),
    consultant_id int             not null references app_user (id),
    end_date      datetime        not null,
    start_date    datetime        not null

)

