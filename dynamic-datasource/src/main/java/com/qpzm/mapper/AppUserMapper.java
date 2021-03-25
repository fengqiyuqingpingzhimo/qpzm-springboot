package com.qpzm.mapper;

import org.apache.ibatis.annotations.Insert;

import com.qpzm.dto.AppUser;

/**  
* @Description: 用户信息数据库交互层
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
public interface AppUserMapper {
	
	@Insert("INSERT INTO APP_USER(ID,NAME) VALUES (#{id},#{name})")
	int insert(AppUser data);

}
