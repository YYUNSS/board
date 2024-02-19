package com.kh.demo.domain.board.dao;

import com.kh.demo.domain.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class BoardDAOImpl implements BoardDAO{

  private final NamedParameterJdbcTemplate template;
  BoardDAOImpl(NamedParameterJdbcTemplate template){
    this.template = template;
  }

  @Override
  public List<Board> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append(" select * from board order by board_id asc ");
    List<Board> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Board.class));

    return list;
  }

  @Override
  public Optional<Board> findById(Long boardId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from board where board_id = :boardId ");
    try{
      Map<String,Long> map = Map.of("boardId",boardId);
      Board board = template.queryForObject(sql.toString(), map, BeanPropertyRowMapper.newInstance(Board.class));
      return Optional.of(board);
    }catch (EmptyResultDataAccessException e){
      return Optional.empty();
    }
  }

  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into board(board_id,title,contents,writer) ");
    sql.append(" values(board_board_id_seq.nextval, :title, :contents, :writer) ");
    SqlParameterSource param = new BeanPropertySqlParameterSource(board);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"board_id", "title", "writer"});
    Long board_id = ((BigDecimal)keyHolder.getKeys().get("board_id")).longValue();

    return board_id;
  }

  //단건 삭제
    @Override
  public int deleteById(Long boardId) {
    StringBuffer sql = new StringBuffer();
      sql.append(" delete from board ");
      sql.append(" where board_id = :boardId ");

      SqlParameterSource param = new MapSqlParameterSource()
              .addValue("boardId", boardId);

      int deletedRowCnt = template.update(sql.toString(), param);

      return deletedRowCnt;
  }
//여러건 삭제
  @Override
  public int deleteByIds(List<Long> boardIds) {
    StringBuffer sql = new StringBuffer();
    sql.append(" delete from board ");
    sql.append(" where board_id in (:boardIds) ");

    Map<String, List<Long>> map = Map.of("boardIds", boardIds);
    int deletedRowCnt = template.update(sql.toString(), map);

    return deletedRowCnt;
  }

  @Override
  public int updateById(Long boardId, Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append(" update board ");
    sql.append(" set title = :title, ");
    sql.append(" contents = :contents, ");
    sql.append(" udate = default ");
    sql.append(" where board_id = :boardId ");

    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("title", board.getTitle())
            .addValue("contents", board.getContents())
            .addValue("boardId", boardId);

    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }
}
