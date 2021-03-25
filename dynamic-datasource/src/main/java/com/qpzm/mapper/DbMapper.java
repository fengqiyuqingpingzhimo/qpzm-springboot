package com.qpzm.mapper;

import org.apache.ibatis.annotations.Select;

import com.qpzm.utils.JdbcUtil;

/**  
* @Description: 和数据库 系统表 相关的执行处理类
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
public interface DbMapper {
	
	@Select(JdbcUtil.ORACLE_VALIDATE)
    int oracleVali();
	
	@Select(JdbcUtil.MYSQL_VALIDATE)
	int mysqlVali();
	
	/**验证oracle数据库的某个用户下某个表是否存在**/
	@Select("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = #{0}")
	int isExistTableOracle(String tableName);
	
	/**验证mysql数据库的某个用户下某个表是否存在**/
	@Select("SELECT count(TABLE_NAME) FROM information_schema.tables WHERE table_schema=#{1} AND table_name=#{0}")
	int isExistTableMysql(String tableName,String tableSchema);
	
	/**验证oracle数据库某个用户下某个表是否存在某个字段**/
	@Select("SELECT COUNT(*) FROM USER_TAB_COLUMNS T WHERE T.TABLE_NAME=#{0} AND T.COLUMN_NAME=#{1}")
	int isExistFieldOracle(String tableName,String field);
	
	/**验证mysql数据库某个用户下某个表是否存在某个字段**/
	@Select("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_SCHEMA=#{0} AND TABLE_NAME=#{1} AND COLUMN_NAME=#{2} ")
	int isExistFieldMysql(String user,String tableName,String field);

}
