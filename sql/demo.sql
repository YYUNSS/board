--테이블삭제
drop table product;

--시퀀스삭제
drop sequence product_product_id_seq;

---------
--상품관리
--------
create table product(
    product_id  number(10),
    pname       varchar(30),
    quantity    number(10),
    price       number(10),
    cdate       timestamp, --생성일시
    udate       timestamp  --수정일시
);
--기본키
alter table product add constraint product_product_id_pk primary key(product_id);

--시퀀스생성
create sequence product_product_id_seq;

--디폴트
alter table product modify cdate default systimestamp; --운영체제 일시를 기본값으로
alter table product modify udate default systimestamp; --운영체제 일시를 기본값으로

--필수 not null
alter table product modify quantity not null;
alter table product modify price not null;

--생성--
insert into product(product_id,pname,quantity,price)
     values(product_product_id_seq.nextval, '컴퓨터', 5, 1000000);

insert into product(product_id,pname,quantity,price)
     values(product_product_id_seq.nextval, '모니터', 5, 500000);

insert into product(product_id,pname,quantity,price)
     values(product_product_id_seq.nextval, '프린터', 1, 300000);
commit;
