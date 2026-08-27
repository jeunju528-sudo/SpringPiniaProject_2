package com.sist.web.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "bootboard")
@Entity
@Getter
@Setter
public class BootBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 번호
	private int no;
	
	private String name;
	
	private String subject;
	
	private String content;
	
	@Column(insertable = true, updatable = false)
	private String password;
	
	@Column(insertable = true, updatable = false, name = "regdate")
	private LocalDateTime regdate;
	
	private int hit;
	
	// 직전에 자동 실행되는 콜백 메서드 지정
	@PrePersist
	public void persist() {
		regdate = LocalDateTime.now();
	}
}
