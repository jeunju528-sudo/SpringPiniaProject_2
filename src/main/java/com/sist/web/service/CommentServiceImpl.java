package com.sist.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.CommentMapper;
import com.sist.web.vo.CommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	
	private final CommentMapper mapper;
	
	@Override
	public List<CommentVO> commentListData(int start, int fno) {
		return mapper.commentListData(start, fno); 
	}

	@Override
	public int commentRowCount(int fno) {
		return mapper.commentRowCount(fno);
	}

	@Override
	public void commentInsert(CommentVO vo) {
		mapper.commentInsert(vo);
	}

}
