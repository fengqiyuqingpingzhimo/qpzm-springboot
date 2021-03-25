package com.qpzm.component;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;
//import com.googosoft.config.db.dynamic.DynamicDataSource;
//import com.googosoft.dao.OptionDbDao;
//import com.googosoft.mapper.DbMapper;
//import com.googosoft.model.db_change_dto;
//import com.googosoft.model.kzt_db_connection;
//import com.googosoft.until.JdbcUtil;
//import com.googosoft.until.Validate;
import com.qpzm.config.dynamic.DynamicDataSource;
import com.qpzm.mapper.DbMapper;
import com.qpzm.utils.JdbcUtil;

/**  
* @Description: 动态切换系统的从数据源
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
@Component
public class DataSourceComponent {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.maxActive}")
    private Integer maxActive;
    @Value("${spring.datasource.minIdle}")
    private Integer minIdle;
    @Value("${spring.datasource.initialSize}")
    private Integer initialSize;
    @Value("${spring.datasource.maxWait}")
    private Long maxWait;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.testOnBorrow}")
    private Boolean testOnReturn;
    
    @Autowired private DbMapper mapper;

//	public DruidXADataSource getWdataSource() {
	public DruidDataSource getWdataSource() {
//		DruidXADataSource wdataSource = new DruidXADataSource();
		DruidDataSource wdataSource = new DruidXADataSource();
		wdataSource.setDriverClassName(driverClass);
		wdataSource.setUrl(url);
		wdataSource.setUsername(user);
		wdataSource.setPassword(password);
		// 连接池配置
		wdataSource.setMaxActive(maxActive);
		wdataSource.setMinIdle(minIdle);
		wdataSource.setInitialSize(initialSize);
		wdataSource.setMaxWait(maxWait);
		wdataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		wdataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		wdataSource.setTestWhileIdle(testWhileIdle);
		wdataSource.setTestOnBorrow(testOnBorrow);
		wdataSource.setTestOnReturn(testOnReturn);
		wdataSource.setPoolPreparedStatements(true);
		wdataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		try {
			wdataSource.setFilters("stat");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wdataSource;
	}
	
	/**
	 * 动态切换数据源 
	 * 2021年3月25日   @_风起于青苹之末
	 * @param dbflag  32位gid  作为数据源标识
	 * @param dbtype  数据库类型
	 * @param url      数据库连接
	 * @param username 数据源用户名称
	 * @param password 数据源用户密码
	 * @return  true:切换成功;false:切换失败
	 */
	public boolean change(String dbflag,String dbtype,String url,String username,String password){
		logger.debug("正在尝试切换系统数据源:");
		logger.debug("dbflag:{}", dbflag);
		logger.debug("dbtype:{},url:{},username:{},password:{}",dbtype, url,username, password);
		String driver= JdbcUtil.getDriver(dbtype);
		if (driver == null) {
			logger.error("未检索到数据库类型为:{}的相关驱动和链接信息!", dbtype);
			return false;
		}
		logger.debug("获取到相关信息:driver:{}", driver);
		try{
			DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
			Map<Object, Object> map = dynamicDataSource.getDataSourceMap();
			boolean flag = false;
			for (Object o : map.keySet()) {
				if (dbflag.equals(o.toString())) {
					flag = true;// 已经存在
					logger.debug("当前数据连接已经在系统数据源内");
					break;
				}
			}
			if (!flag) {
//				DruidXADataSource rdataSource = getWdataSource();//复制配置
				DruidDataSource rdataSource = getWdataSource();//复制配置
				rdataSource.setDriverClassName(driver);
				rdataSource.setUrl(url);
				rdataSource.setUsername(username);
				rdataSource.setPassword(password);
				rdataSource.setBreakAfterAcquireFailure(true);//连接出错后不再进行二次连接
//				rdataSource.setDefaultAutoCommit(false);//动态数据源 执行事务手动提交
				
//				AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//				xaDataSource.setXaDataSource(rdataSource);
////				xaDataSource.setUniqueResourceName(UuidUtil.get32UUID());
//				xaDataSource.setUniqueResourceName(dbflag);
//				map.put(dbflag, xaDataSource);
				
				map.put(dbflag, rdataSource);
				dynamicDataSource.setTargetDataSources(map);
			} 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("动态切换从数据源失败,从数据源主要信息:类型:{},驱动:{},链接:{}",dbtype,driver, url);
			return false;
		}
		DynamicDataSource.setDBKey(dbflag);
		logger.debug("开始进行简单sql验证!");
		try{
			if(JdbcUtil.ORACLE.equals(dbtype)){
				mapper.oracleVali();
			}else if(JdbcUtil.MYSQL.equals(dbtype)){
				mapper.mysqlVali();
			}else{
				logger.error("动态切换从数据源失败,未配置:{}数据库的sql验证",dbtype);
				DynamicDataSource.clearDBKey();
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("动态切换从数据源失败,sql验证未通过验证,可能连接存在问题");
			DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
			Map<Object, Object> map = dynamicDataSource.getDataSourceMap();
			map.remove(dbflag);
			dynamicDataSource.setTargetDataSources(map);
			DynamicDataSource.clearDBKey();
			return false;
		}
		logger.debug("当前数据库连接可用!");
		return true;
	}

}
