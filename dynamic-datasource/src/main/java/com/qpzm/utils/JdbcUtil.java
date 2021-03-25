package com.qpzm.utils;
/**  
* @Description: 数据库常量工具类
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
public class JdbcUtil {
	
	public static final String ORACLE            = "oracle";
    public static final String ORACLE_DRIVER     = "oracle.jdbc.OracleDriver";
    public static final String ORACLE_URL        = "jdbc:oracle:thin:@%s:%s:%s";
    public static final String ORACLE_VALIDATE   ="SELECT 1 FROM DUAL";
    
    public static final String MYSQL             = "mysql";
    public static final String MYSQL_DRIVER      = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL         = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public static final String MYSQL_VALIDATE    = "SELECT 1 ";
    
	/**
	 * 根据数据库种类获取到对应驱动
	 * 2021年3月25日下午3:36:51 @_风起于青苹之末
	 */
	public static String getDriver(String dbType) {
		switch (dbType) {
			case ORACLE:
				return ORACLE_DRIVER;
			case MYSQL:
				return MYSQL_DRIVER;
			default:
				return null;
		}
	}
}
