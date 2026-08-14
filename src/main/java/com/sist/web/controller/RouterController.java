package com.sist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RouterController {
	@GetMapping("/")
	public String main_main(Model model) {
		model.addAttribute("main_html", "main/home");
		return "main/main";
	}
	@GetMapping("/food/detail/{no}")
	public String food_detail(@PathVariable(value = "no") int no, Model model) {
		System.out.println("no :: " + no);
		
		model.addAttribute("no", no);
		model.addAttribute("main_html", "food/detail");
		
		return "main/main";
	}
}
