package com.qpzm.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qpzm.config.dynamic.DynamicDataSource;
import com.qpzm.config.dynamic.DynamicDataSourceTransactionFactory;

/**  
* @Description: 系统动态数据源配置,动态据源采用动态配置,会根据系统配置的数据库信息动态扩展数据库连接池
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
@Configuration
@MapperScan(basePackages = "com.qpzm.dao_dynamic", sqlSessionTemplateRef = "dynamicSqlSessionTemplate")
public class DynamicDataSourceConfig {
	
	@Bean(name = "dynamicDataSource")
	public DynamicDataSource dynamicDataSource() {
		DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
		Map<Object,Object> map = new HashMap<>();
	    dynamicDataSource.setTargetDataSources(map);
		return dynamicDataSource;
	}
	
	@Bean(name = "dynamicSqlSessionFactory")
	public SqlSessionFactory setSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource)throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setTransactionFactory(new DynamicDataSourceTransactionFactory());//保证从数据源 在@Transactional事务注解下能够正常切换数据库
		return bean.getObject();
	}
	

	@Bean(name = "dynamicSqlSessionTemplate")
	public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("dynamicSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
