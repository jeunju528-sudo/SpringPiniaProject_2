package com.sist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sist.web.service.FoodService;
import com.sist.web.vo.FoodVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RouterController {
	
	private final FoodService foodService;
	
	@GetMapping("/")
	public String main_main(Model model) {
		model.addAttribute("main_html", "main/home");
		return "main/main";
	}

	@GetMapping("/food/detail_before/{no}")
	public String food_detail_before(@PathVariable(value = "no") int no, HttpServletResponse response,
			RedirectAttributes ra) {
		Cookie cookie = new Cookie("food_" + no, String.valueOf(no));
		cookie.setMaxAge(60 * 60 * 24);
		cookie.setPath("/");
		response.addCookie(cookie); // 브라우저로 전송

		ra.addAttribute("no", no);
		return "redirect:/food/detail";
	}

	@GetMapping("/food/detail")
	public String food_detail(@RequestParam(value = "no") int no, Model model) {
		
		FoodVO vo = foodService.foodDetailData(no);
		
		model.addAttribute("vo", vo);
		model.addAttribute("main_html", "food/detail");

		return "main/main";
	}

	@RequestMapping("/member/login")
	public String member_login(Model model) {
		model.addAttribute("main_html", "member/login");
		return "main/main";
	}
}
