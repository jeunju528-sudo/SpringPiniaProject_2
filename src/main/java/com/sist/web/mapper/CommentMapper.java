package com.sist.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.CommentVO;

@Repository
@Mapper
public interface CommentMapper {

	public List<CommentVO> commentListData(@Param("start") int start, @Param("fno") int fno);

	public int commentRowCount(int fno);

	public void commentInsert(CommentVO vo);
	
	@Delete("DELETE FROM piniacomment WHERE no = #{no}")
	public void commentDelete(@Param("no")int no);
	
	@Update("UPDATE piniacomment SET msg = #{msg} WHERE no = #{no}")
	public void commentUpdate(@Param("msg") String msg, @Param("no") int no);
	
}
