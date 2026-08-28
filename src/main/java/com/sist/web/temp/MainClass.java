package com.sist.web.temp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainClass {
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.connect("https://rank.ezme.net").get();
			Elements words = doc.select(".rank_word");
			Elements images = doc.select(".rank_img");
			for (int i = 0; i < words.size(); i++) {
				String w = words.get(i).text();
				String img = images.get(i).attr("data-pagespeed-lazy-src");
				// RealFindDataVO vo=new RealFindDataVO();
				// vo.setWord(w);
				// vo.setImages(img);
				// list.add(vo);
				System.out.println(w);
				System.out.println(img);
				System.out.println("======================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
