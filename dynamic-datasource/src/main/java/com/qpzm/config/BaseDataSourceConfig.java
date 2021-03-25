package com.qpzm.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

/**  
* @Description: 系统第一数据源配置
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
@Configuration
@MapperScan(basePackages ={"com.qpzm.dao"} , sqlSessionTemplateRef = "baseSqlSessionTemplate")
public class BaseDataSourceConfig {
	
	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean(initMethod = "init", destroyMethod = "close", name = "baseDataSource")
	@Primary // 多数据源配置相关 单数据源无需该注解
	public DruidDataSource dataSource() {
		DruidDataSource ds = new DruidDataSource();
        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter());
        ds.setProxyFilters(filters);//类型是List<com.alibaba.druid.filter.Filter>，如果同时配置了filters和proxyFilters，是组合关系，并非替换关系
		return ds;
	}
	
	@Bean(name = "baseSqlSessionFactory")
	public SqlSessionFactory setSqlSessionFactory(@Qualifier("baseDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Bean(name = "baseSqlSessionTemplate")
	public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("baseSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	public WallFilter wallFilter() {
		WallFilter wallFilter = new WallFilter();
		wallFilter.setConfig(wallConfig());
		return wallFilter;
	}

	@Bean
	public WallConfig wallConfig() {
		WallConfig wallConfig = new WallConfig();
		wallConfig.setMultiStatementAllow(true);// 允许一次执行多条语句
		wallConfig.setNoneBaseStatementAllow(true);// 允许非基本语句的其他语句
		return wallConfig;
	}

	@Bean(name = "locationTransaction")//主库事务/本地事务  与jta区别
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier("baseDataSource") DataSource dataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

}
