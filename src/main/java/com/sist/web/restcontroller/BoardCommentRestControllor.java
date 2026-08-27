package com.sist.web.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.mapper.BoardCommentMapper;
import com.sist.web.vo.BootCommentVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardCommentRestControllor {
	private final BoardCommentMapper bMapper;
	private final SimpMessagingTemplate template;
	
	public Map<String, Object> commonsListData(int page, int board_no) {
		Map<String, Object> map = new HashMap<>();
		int start = (page - 1) * 10;
		map.put("start", start);
		map.put("board_no", board_no);

		List<BootCommentVO> list = bMapper.boardCommentListData(map);
		int count = bMapper.boardCommentCount(board_no);
		int totalpage = (int) (Math.ceil(count / 10.0));

		map = new HashMap<>();
		map.put("list", list);
		map.put("curpage", page);
		map.put("totalpage", totalpage);
		map.put("count", count);

		return map;
	}

	@Async
	@GetMapping("/reply/list_vue")
	public ResponseEntity<Map<String, Object>> board_list(@RequestParam("board_no") int board_no,
			@RequestParam("page") int page) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = commonsListData(page, board_no);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}

	@Async
	@PostMapping("/reply/insert_vue")
	public ResponseEntity<Map<String, Object>> reply_insert(@RequestBody BootCommentVO vo, HttpSession session){
		Map<String, Object> map = new HashMap<>();
		try {
			String userid = (String)session.getAttribute("userid");
			String username = (String) session.getAttribute("username");
			vo.setUserid(userid);
			vo.setName(username);
			bMapper.boardCommentInsert(vo);
			map = commonsListData(vo.getPage(), vo.getBoard_no());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	

	@PostMapping("/reply/rereply_insert_vue")
	public ResponseEntity<Map> rereply_insert_vue(@RequestBody BootCommentVO vo, HttpSession session){
		Map<String, Object> map = new HashMap<>();

		try {
			BootCommentVO pvo = bMapper.boardCommentParentInfoData(vo.getNo());
			bMapper.bootCommentStepIncrement(pvo.getGroup_id(), pvo.getGroup_step());
			vo.setGroup_id(pvo.getGroup_id());
			vo.setGroup_step(pvo.getGroup_step()+1);
			vo.setGroup_tab(pvo.getGroup_tab()+1);
			vo.setRoot(vo.getNo());
			vo.setUserid((String)session.getAttribute("userid"));
			vo.setName((String)session.getAttribute("username"));
			bMapper.boardCommentReReply(vo);
			bMapper.boardDepthIncrement(vo.getNo());
			
			if(!pvo.getUserid().equals(vo.getUserid())) {
				template.convertAndSend("/sub/notice/"+pvo.getUserid(),
						"[📢댓글 알림] "+vo.getUserid()+"님이 댓글을 달았습니다!!");
			}
			
			map = commonsListData(vo.getPage(), vo.getBoard_no());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(map);
	}
}
