package com.sist.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.CommentVO;

@Repository
@Mapper
public interface CommentMapper {

	public List<CommentVO> commentListData(@Param("start") int start, @Param("fno") int fno);

	public int commentRowCount(int fno);

	public void commentInsert(CommentVO vo);
	
}
