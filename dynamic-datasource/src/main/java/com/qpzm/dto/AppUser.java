package com.qpzm.dto;
/**  
* @Description: 用户实体类
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
public class AppUser {
	
	/**
	 * 用户id
	 */
	private String id;
	
	/**
	 * 用户姓名
	 */
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppUser() {
		super();
	}

	public AppUser(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
