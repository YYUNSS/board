package com.kh.demo.web;

import com.kh.demo.domain.board.svc.BoardSVC;
import com.kh.demo.domain.entity.Board;
import com.kh.demo.web.form.board.AddForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
//@Controller
@RequestMapping("/boardlist")
public class BoardControllerV1 {
  private BoardSVC boardSVC;

  BoardControllerV1(BoardSVC boardSVC) {
    this.boardSVC = boardSVC;
  }

  @GetMapping
  public String findAll(Model model) {
    List<Board> list = boardSVC.findAll();
    model.addAttribute("list", list);

    return "board/all";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("addForm", new AddForm());
    return "board/add";
  }

  @PostMapping("/add")  // Get, http://localhost:9080//Boardlist/add
  public String add(
          AddForm addForm,
          Model model,
          RedirectAttributes redirectAttributes
  ) {
    log.info("addForm={}", addForm);
    Board board = new Board();
    board.setTitle(addForm.getTitle());
    board.setContents(addForm.getContents());
    board.setWriter(addForm.getWriter());

    Long boardId = boardSVC.save(board);
    log.info("boardId={}", boardId);
    redirectAttributes.addAttribute("bid", boardId);
    return "redirect:/boardlist/{bid}/detail";
  }

  //조회
  @GetMapping("/{bid}/detail")
  public String findById(@PathVariable("bid") Long boardId, Model model) {
    Optional<Board> findedBoard = boardSVC.findById(boardId);
    Board board = findedBoard.orElseThrow();
    model.addAttribute("board", board);
    return "board/viewBoard";
  }

  //단건 삭제
  @GetMapping("/{bid}/del")
  public String deleteById(@PathVariable("bid") Long boardId) {
    log.info("deleteById={}", boardId);

    int deletedRowCnt = boardSVC.deleteById(boardId);
    return "redirect:/boardlist"; // GET http://localhost:9080/boardlist/
  }

  //여러건 삭제
  @PostMapping("/del")
  public String deleteByIds(@RequestParam("bids") List<Long> bids) {
    log.info("deleteByIds={}", bids);
    int deletedRowCnt = boardSVC.deleteByIds(bids);
    return "redirect:/boardlist";
  }

  //수정 양식
  @GetMapping("/{bid}/edit")
  public String updateForm(
          @PathVariable("bid") Long boardId,
          Model model){
    Optional<Board> optionalBoard = boardSVC.findById(boardId);
    Board findedboard = optionalBoard.orElseThrow();
    model.addAttribute("board", findedboard);
    return "board/updateForm";
  }
  //수정 처리
  @PostMapping("{bid}/edit")
  public String update(
          @PathVariable("bid") Long boardId,
          @RequestParam("title")String title,
          @RequestParam("contents")String contents,
          RedirectAttributes redirectAttributes){

    Board board = new Board();
    board.setTitle(title);
    board.setContents(contents);
    int updateRowCnt = boardSVC.updateById(boardId, board);

    redirectAttributes.addAttribute("bid",boardId);
    return "redirect:/boardlist/{bid}/detail";
  }
}
