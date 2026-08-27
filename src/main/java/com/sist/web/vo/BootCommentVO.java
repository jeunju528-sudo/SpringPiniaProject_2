package com.sist.web.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BootCommentVO {
	private int no;
	private int board_no;
	private String userid;
	private String name;
	private String msg;
	private Date regdate;
	private int group_id;
	private int group_step;
	private int group_tab;
	private int root;
	private int depth;
	private String dbday;
	private int page;
}
