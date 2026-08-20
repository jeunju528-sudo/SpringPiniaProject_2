package com.sist.web.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CommentVO {
	private int no;
	private int fno;
	private int page;
	private String id;
	private String name;
	private String msg;
	private String dbday;
	private Date regdate;
}
