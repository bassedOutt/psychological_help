begin transaction;
alter table consultation
    drop column price;
alter table consultation
    drop column user_details;
alter table consultation
    add user_email varchar(100);
alter table consultation
    add pib varchar(100);
commit transaction;
