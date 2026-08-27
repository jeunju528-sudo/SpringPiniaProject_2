package com.sist.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sist.web.entity.BootBoard;
import com.sist.web.repository.BootBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl {
	private final BootBoardRepository repository;
	
	public Page<BootBoard> findAll(Pageable pg){
		return repository.findAll(pg);
	}
	
	public int boardTotalpage() {
		return (int)(Math.ceil(repository.count()/10.0));
	}
	
	public BootBoard findByNo(int no) {
		return repository.findByNo(no);
	}
	
	public void save(BootBoard vo) {
		repository.save(vo);
	}
	
}
