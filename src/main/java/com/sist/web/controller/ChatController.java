package com.sist.web.controller;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate template;
	// 접속자 저장 공간
	private final Set<String> users = ConcurrentHashMap.newKeySet();
	
	
	// 전체 채팅
	@MessageMapping("/chat/public")
	@SendTo("/topic/chat")
	public ChatMessage publicChat(ChatMessage msg, Principal p) {
		msg.setSender(p.getName());
		return msg;
	}

	@MessageMapping("/chat/private")
	public void privateChat(ChatMessage msg, Principal p) {

		String sender = p.getName();

		msg.setSender(sender);
		
		// 받는 사람한테도 메세지를 보여주고
		template.convertAndSendToUser(msg.getReceiver(), "/queue/chat", msg);

		// 작성한 사람한테도 메세지를 보여줌
		template.convertAndSendToUser(sender, "/queue/chat", msg);
	}

	@MessageMapping("/chat/join")
	public void join(Principal p) {
		String username = p.getName();
		users.add(username);
		template.convertAndSend("/topic/users", users);
	}

	@GetMapping("/chat/chat")
	public String chat_page(Model model) {
		model.addAttribute("main_html", "chat/chat");
		return "main/main";
	}
}