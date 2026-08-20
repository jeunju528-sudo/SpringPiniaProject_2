package com.sist.web.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.service.CommentService;
import com.sist.web.vo.CommentVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentRestController {
	private final CommentService service;

	public Map<String, Object> commonsData(int page, int fno) {
		Map<String, Object> map = new HashMap<>();

		int start = (page - 1) * 10;
		List<CommentVO> list = service.commentListData(start, fno);
		int count = service.commentRowCount(fno);
		int totalpage = (int)(Math.ceil(count/10.0));
		
		map.put("rList", list);
		map.put("count", count);
		map.put("curpage", page);
		map.put("totalpage", totalpage);
		
		return map;
	}
	
	@GetMapping("/comment/list_vue")
	public ResponseEntity<Map<String, Object>> comment_list(@RequestParam("page")int page, @RequestParam("fno") int fno){
		Map<String, Object> map = new HashMap<>();
		
		try {
			map = commonsData(page, fno);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/comment/insert_vue")
	// vue에서 {} 로 보내는 값을 자바 객체로 받기 위해서는 @RequestBody 붙여줘야함
	public ResponseEntity<Map> comment_insert(@RequestBody CommentVO vo, HttpSession session){
		Map<String, Object> map = new HashMap<>();
		
		try {
			String id = (String) session.getAttribute("userid");
			String name = (String) session.getAttribute("username");
			vo.setId(id);
			vo.setName(name);			
			service.commentInsert(vo);
			map = commonsData(vo.getPage(), vo.getFno());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return ResponseEntity.ok(map);
	}
}
