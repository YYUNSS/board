--테이블 삭제
drop table board;

--시퀀스 삭제
drop sequence board_board_id_seq;

--테이블 생성
create table board(
board_id   number(10),     -- 게시글 아이디
title       varchar(100),    -- 제목
contents    varchar(1000),           -- 내용
writer      varchar(100),    -- 작성자
cdate       timestamp,      -- 작성일자
udate       timestamp);     -- 수정일자

--기본키
alter table board add constraint board_board_id_pk primary key(board_id);

--시퀀스 생성
create sequence board_board_id_seq;

--디폴트값 부여
alter table board modify cdate default systimestamp;
alter table board modify udate default systimestamp;

insert into board(board_id,title,contents,writer)
     values(board_board_id_seq.nextval, '제목1', '내용1', '작성자1');

insert into board(board_id,title,contents,writer)
     values(board_board_id_seq.nextval, '제목2', '내용2', '작성자2 ');
     
insert into board(board_id,title,contents,writer)
     values(board_board_id_seq.nextval, '제목3', '내용3', '작성자3');


commit;