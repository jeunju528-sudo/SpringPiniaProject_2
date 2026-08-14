package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.FoodMapper;
import com.sist.web.vo.FoodVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{
	
	private final FoodMapper mapper;

	@Override
	public List<FoodVO> foodListData(int page) {
		return null;
	}

	@Override
	public int foodTotalPage() {
		return 0;
	}

	@Override
	public FoodVO foodDetailData(int no) {
		return null;
	}

	@Override
	public int[] foodPages(int page) {
		return null;
	}

}
