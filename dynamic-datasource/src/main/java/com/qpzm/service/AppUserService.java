package com.qpzm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qpzm.component.DataSourceComponent;
import com.qpzm.dao.AppUserDao;
import com.qpzm.dao_dynamic.AppUser_DynamicDao;
import com.qpzm.dto.AppUser;
import com.qpzm.utils.JdbcUtil;

/**  
* @Description: 用户信息中间处理层
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
@Service
public class AppUserService {
	
	@Autowired private AppUserDao dao;
	
	@Autowired private AppUserDao appDao;
	
	@Autowired private DataSourceComponent component;
	
	@Autowired private AppUser_DynamicDao dynamicDao;
	
	@Transactional
	public void insert(){
		 dao.insert(new AppUser("first", "user"));
		 appDao.insert(new AppUser("seconde", "user"));
//		 throw new RuntimeException("主动抛出异常");
	}
	
	public void dynamicTest(){
		this.component.change("1", JdbcUtil.ORACLE, "jdbc:oracle:thin:@192.168.10.169:1521:shitan", "KS_OAAUTO", "KS_OAAUTO");
		this.dynamicDao.insert(new AppUser("dynamic", "user"));
		
		this.component.change("2", JdbcUtil.ORACLE, "jdbc:oracle:thin:@192.168.10.1:1521:shitan", "ai_platform", "ai_platform");
		this.dynamicDao.insert(new AppUser("dynamic2", "user"));
		
		this.component.change("1", JdbcUtil.ORACLE, "jdbc:oracle:thin:@192.168.10.169:1521:shitan", "KS_OAAUTO", "KS_OAAUTO");
		this.dynamicDao.insert(new AppUser("dynamic1", "user"));
	}
	
	

}
