package com.qpzm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qpzm.service.AppUserService;

/**  
* @Description: 单元测试
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	
	@Autowired AppUserService service;
	
	@Test
	public void test() {
//		this.service.insert();
		this.service.dynamicTest();
	}

}
