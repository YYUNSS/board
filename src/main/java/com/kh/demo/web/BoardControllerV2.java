package com.kh.demo.web;

import com.kh.demo.domain.board.svc.BoardSVC;
import com.kh.demo.domain.entity.Board;
import com.kh.demo.web.form.board.AddForm;
import com.kh.demo.web.form.board.UpdateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/boardlist")
public class BoardControllerV2 {
  private BoardSVC boardSVC;

  BoardControllerV2(BoardSVC boardSVC) {
    this.boardSVC = boardSVC;
  }

  @GetMapping
  public String findAll(Model model) {
    List<Board> list = boardSVC.findAll();
    model.addAttribute("list", list);

    return "board/all";
  }
  //게시물 등록 양식
  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("addForm", new AddForm());
    return "board/add";
  }
  //게시물 등록 처리
  @PostMapping("/add")  // Get, http://localhost:9080//Boardlist/add
  public String add(
          AddForm addForm,
          Model model,
          RedirectAttributes redirectAttributes
  ) {
    log.info("addForm={}", addForm);
    //유효성 체크
    //필드레벨
    //1-1) 게시글 제목
    String pattern = "^[a-zA-Z0-9가-힣\\W_-]{1,10}$";
    if (!Pattern.matches(pattern, addForm.getTitle())) {
      model.addAttribute("board", addForm);
      model.addAttribute("err_title", "1~10자 입력가능");
      return "board/add";
    }
    //1-2) 게시글 내용
    pattern = "^[a-zA-Z0-9가-힣\\W_-]{1,100}$";
    if (!Pattern.matches(pattern, addForm.getContents())) {
      model.addAttribute("board", addForm);
      model.addAttribute("err_contents", "1~100자 입력가능");
      return "board/add";
    }
    //1-3) 작성자
    pattern = "^[a-zA-Z0-9가-힣\\W_-]{1,10}$";
    if (!Pattern.matches(pattern, addForm.getWriter())) {
      model.addAttribute("board", addForm);
      model.addAttribute("err_writer", "1~10자 입력가능");
      return "board/add";
    }
    //게시글 등록
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
  public String update(@PathVariable("bid") Long boardId,
                       UpdateForm updateForm,
                       RedirectAttributes redirectAttributes,
                       Model model) {
    //유효성 검사
    //필드레벨
    //1-1) 게시글 제목
    String pattern = "^[a-zA-Z0-9가-힣\\W_-]{1,10}$";
    if (!Pattern.matches(pattern, updateForm.getTitle())) {
      model.addAttribute("board", updateForm);
      model.addAttribute("err_title", "1~10자 입력가능");
      return "board/updateForm";
    }
    //1-2) 게시글 내용
    pattern = "^[a-zA-Z0-9가-힣\\W_-]{1,100}$";
    if (!Pattern.matches(pattern, updateForm.getContents())) {
      model.addAttribute("board", updateForm);
      model.addAttribute("err_contents", "1~100자 입력가능");
      return "board/updateForm";
    }

    //정상처리
    Board board = new Board();
    board.setTitle(updateForm.getTitle());
    board.setContents(updateForm.getContents());

    int updateRowCnt = boardSVC.updateById(boardId, board);
    redirectAttributes.addAttribute("bid", boardId);
    return "redirect:/boardlist/{bid}/detail";
  }
}
