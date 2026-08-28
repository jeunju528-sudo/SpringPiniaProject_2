package com.sist.web.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sist.web.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeConsumer {
	private final SimpMessagingTemplate template; // 읽은 메세지를 STOMP를 이용하여 전송함
	
	
	/*
	 * Spring Kafka = Spring이 Apache Kafka를 쉽게 사용하도록 감싼 라이브러리
	 * 내부적으로 Apache의 Kafka Client(Kafka-clients)를 감싸고있음
	 * kafka-clients : 실제 브로커(카프카 브로커(서버))와 통신하는 클라이언트
	 * */
	@KafkaListener(
		topics = "notice-topic",
		groupId = "notice-group"
	)
	public void consumerNotice(ChatMessage notice) {
		// Kafka에서 메세지가 들어오면 Spring에서 자동 호출하게함
		System.out.println("Kafka 알림 수신 : " + notice.toString());
		String dest = "/sub/notice/"+notice.getReceiver();
		template.convertAndSend(
				dest,
				notice.getMessage()
		);
		System.out.println("STOMP 알림 전송 완료 : " + dest);
		
	}
	
}
