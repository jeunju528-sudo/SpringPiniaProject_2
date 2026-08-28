package com.sist.web.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sist.web.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// producer : 메세지를 보내는 역할 (메세지 생성)
public class NoticeProducer {
	// KafkaTemplate : 카프카로 메세지를 보내는 객체
	// <String, ChatMessage> : <키 타입(파티션 라우팅 할 때 사용), 메세지 본문 타입>
	private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
	
	private static final String TOPIC = "notice-topic";
	
	// Controller에서 호출!
	public void sendNotice(ChatMessage notice) {
		kafkaTemplate.send(
				TOPIC, // 토픽명
				notice.getReceiver(), // 메세지 키, 파티션을 고를 때 사용됨
				notice // 본문
		);
		/*
		 * 메세지 키는 파티션을 고를 때 사용되는데  
		 * 파티션 결정은 =>  파티션 = hash(userId) % 파티션 갯수
		 * hash(user1) % 3 = 0 → 파티션 0
		 * hash(user2) % 3 = 2 → 파티션 2
		 * hash(user3) % 3 = 0 → 파티션 0
		 * 이렇게 됨
		 * 그래서 항상 값이 바뀌지 않는 id 같은걸로 메세지 키를 보통 설정하게 됨!!
		 * */
		System.out.println("Kafka 알림 전송 : " + notice.toString());
	}
	
}
