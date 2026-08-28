package com.sist.web.manager;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sist.web.vo.RealFindVO;

@Component
public class RealFindWordTask {
	
	private static int index = 1;

	@Async
	@Scheduled(fixedRate = 60*1*1000) // 1분에 한 번 씩
	public void task() {
		List<RealFindVO> list = DataCollection.dataCollection();
		for(RealFindVO vo : list) {
			System.out.println("========== index : " + index + " ===========");
			System.out.println("Rank : " + vo.getRank());
			System.out.println("Word : " + vo.getWord());
		}
		index++;
	}
}
