package com.sist.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.entity.BootBoard;
import com.sist.web.service.BoardServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	private final BoardServiceImpl service;

	@GetMapping("/board/list")
	public String board_list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		final int ROW_SIZE = 10;
		Pageable pg = PageRequest.of(page - 1, ROW_SIZE, Sort.by(Sort.Direction.DESC, "no"));
		Page<BootBoard> pList = service.findAll(pg);
		List<BootBoard> list = new ArrayList<>();
		if (pList != null && pList.hasContent()) {
			list = pList.getContent();
		}
		int totalpage = service.boardTotalpage();

		model.addAttribute("list", list);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("curpage", page);
		model.addAttribute("main_html", "board/list");
		return "main/main";
	}
	
	@GetMapping("/board/insert")
	public String board_insert(Model model) {
		model.addAttribute("main_html", "board/insert");
		return "main/main";
	}
	
	@PostMapping("/board/insert_ok")
	public String board_insert_ok(@ModelAttribute("vo") BootBoard vo) {
		service.save(vo);
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/detail")
	public String board_detail(@RequestParam("no")int no, Model model, HttpSession session) {
		BootBoard vo = service.findByNo(no);
		model.addAttribute("vo", vo);
		
		vo.setHit(vo.getHit()+1);
		service.save(vo);
			
		model.addAttribute("no", no);
		model.addAttribute("main_html", "board/detail");
		return "main/main";
	}
}
