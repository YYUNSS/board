package com.kh.demo.web.form.board;

import lombok.Data;

@Data
public class UpdateForm {
  private Long boardId;
  private String title;
  private String contents;
  private String writer;
}
