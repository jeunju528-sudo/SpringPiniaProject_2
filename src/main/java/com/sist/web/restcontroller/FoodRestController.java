package com.sist.web.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.service.FoodService;
import com.sist.web.vo.FoodVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FoodRestController {
	private final FoodService service;
	// ResponseEntity : Spring Framework(Web MVC)에서 HTTP 요청에 대한 응답 전체(HTTP 상태 코드, 응답 헤더, 응답 본문)를 개발자가 직접 제어하고 구성할 수 있도록 지원하는 객체
	@GetMapping("/food/list_vue")
	public ResponseEntity<Map<String, Object>> food_list(@RequestParam(value = "page", defaultValue = "1") int page) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<FoodVO> list = service.foodListData(page);
			int[] pages = service.foodPages(page);
			map.put("list", list);
			map.put("curpage", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorBody = new HashMap<>();
			errorBody.put("message", "서버 처리 중 오류가 발생했습니다.");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
		}
		/*
		 * 리턴방식 2가지
		 * 1. 생성자 직접 호출 -> 생성자의 매개변수 순서를 맞춰서 반환, 매개변수가 많아지면 헷갈림
		 * return new ResponseEntity<>(body, header, HttpStatus.CREATED);
		 * 2. Builder 패턴 사용 -> .ok() 같은 자주 사용하는 메서드 제공
		 * return ResponseEntity.status(200).header().body().build();
		 * */
		
		return ResponseEntity.ok(map);
	}
}
