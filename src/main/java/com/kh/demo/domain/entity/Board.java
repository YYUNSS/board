package com.kh.demo.domain.entity;
// wrapper class
// byte->Byte, short->Short, char->Character, int->Integer, long->Long
// boolean->Boolean, double->Double, float->Float

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
  private Long boardId;         //게시글 아이디 board_id	NUMBER(10,0)
  private String title;           //제목      title     	VARCHAR2(50)
  private String contents;        //내용      contents 	VARCHAR2(100)
  private String writer;          //작성자     writer    VARCHAR2(10)
  private LocalDateTime cdate;     //생성일시  cdate      TIMESTAMP(6)
  private LocalDateTime udate;    //수정일시   UDATE	     TIMESTAMP(6)
}
