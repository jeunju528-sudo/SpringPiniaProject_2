package com.sist.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.FoodVO;

@Mapper
@Repository
public interface FoodMapper {
	/*
	<select id="foodListData" resultType="com.sist.web.vo.FoodVO" parameterType="int">
		SELECT no, name, poster, address
		 FROM food
		ORDER BY no DESC
		OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY
	</select>
	 * */
	public List<FoodVO> foodListData(int start);
	
	@Select("SELECT CEIL(COUNT(*)/12.0) FROM food ")
	public int foodTotalPage();
	
	@Select("SELECT * FROM food WHERE no = #{no}")
	public FoodVO foodDetailData(int no);
	
	@Update("UPDATE food SET hit = hit+1 WHERE no = #{no}")
	public void foodHitIncrement(int no);
	
	
}
