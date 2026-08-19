package com.sist.web.vo;

import lombok.Data;

@Data
public class MemberVO {
	private String userid;
	private String username;
	private String userpwd;
	// 휴면계정 체크
	private int enable;
	private String sex;
}
