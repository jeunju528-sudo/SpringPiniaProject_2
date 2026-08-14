package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.FoodMapper;
import com.sist.web.vo.FoodVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

	private final FoodMapper mapper;

	@Override
	public List<FoodVO> foodListData(int page) {
		int start = (page - 1) * 12;
		return mapper.foodListData(start);
	}

	@Override
	public int foodTotalPage() {
		return mapper.foodTotalPage();
	}

	@Override
	public FoodVO foodDetailData(int no) {
		mapper.foodHitIncrement(no);
		return mapper.foodDetailData(no);
	}

	@Override
	public int[] foodPages(int page) {
		int totalpage = mapper.foodTotalPage();
		final int BLOCK = 10;
		int startpage = ((page - 1) / BLOCK * BLOCK) + 1;
		int endpage = ((page - 1) / BLOCK * BLOCK) + BLOCK;
		if (endpage > totalpage)
			endpage = totalpage;
		int[] pages = { page, totalpage, startpage, endpage };
		return pages;
	}

}
