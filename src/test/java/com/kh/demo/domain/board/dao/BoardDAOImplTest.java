package com.kh.demo.domain.board.dao;

import com.kh.demo.domain.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class BoardDAOImplTest {
  @Autowired
  BoardDAO boardDAO;

  // 게시글 작성
  @Test
  @DisplayName("게시글 작성")
  void save() {
    Board board = new Board();
    board.setTitle("제목33");
    board.setContents("내용33");
    board.setWriter("작성자33");

    Long boardId = boardDAO.save(board);
    log.info("boardId={}", boardId);
  }

  //게시글 조회
  @Test
  @DisplayName("게시글 조회")
  void findById() {
    Long boardId = 141L;
    Optional<Board> findedboard = boardDAO.findById(boardId);
    Board board = findedboard.orElse(null);
    log.info("board={}", board);
  }

  //단건 삭제
  @Test
  @DisplayName("단건 삭제")
  void deleteById() {
    Long bid = 101L;
    int deletedRowCnt = boardDAO.deleteById(bid);
    log.info("삭제건수={}", deletedRowCnt);
  }

  //여러건 삭제
  @Test
  @DisplayName("여러건 삭제")
  void deletebyIds() {
    List<Long> ids = new ArrayList<>();
    ids.add(161L);
    ids.add(162L);
    int deletedRowCnt = boardDAO.deleteByIds(ids);
    log.info("삭제건수={}", deletedRowCnt);
  }

  //전체 목록
  @Test
  @DisplayName("목록")
  void findAll() {
    List<Board> list = boardDAO.findAll();
    for (Board board : list) {
      log.info("board={}", board);
    }
    log.info("size={}", list.size());
  }

  @Test
  @DisplayName("게시글 수정")
  void updateById() {
    Long boardId = 121L;
    Board board = new Board();

    board.setTitle("제목 수정");
    board.setContents("내용 수정");

    int updatedRowCnt = boardDAO.updateById(boardId, board);
    if (updatedRowCnt == 0) {
      Assertions.fail("변경 내역이 없습니다.");
    }
    Optional<Board> optionalBoard = boardDAO.findById(boardId);
    if (optionalBoard.isPresent()) {
      Board findedBoard = optionalBoard.get();
      Assertions.assertThat(findedBoard.getTitle()).isEqualTo("제목 수정");
      Assertions.assertThat(findedBoard.getContents()).isEqualTo("내용 수정");
    } else {
      Assertions.fail("해당 게시물이 존재하지 않습니다.");
    }
  }
}
