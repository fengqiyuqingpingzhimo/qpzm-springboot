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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;

/**  
* @Description: 系统第二数据源配置
* @Author: 风起于青苹之末
* @Date: 2021年3月23日
*/
@Configuration
@MapperScan( basePackages ={"com.qpzm.mapper"}, sqlSessionTemplateRef = "secondSqlSessionTemplate")
public class SecondDataSourceConfig {
	
//	@ConfigurationProperties(prefix = "kettle.datasource")
	@ConfigurationProperties(prefix = "second.datasource")
	@Bean(initMethod = "init", destroyMethod = "close", name = "secondDataSource")
	public DruidDataSource dataSource() {
		DruidDataSource ds = new DruidDataSource();
        List<Filter> filters = new ArrayList<>();
//        filters.add(wallFilter());
        ds.setProxyFilters(filters);//类型是List<com.alibaba.druid.filter.Filter>，如果同时配置了filters和proxyFilters，是组合关系，并非替换关系
		return ds;
	}
	
	@Bean(name = "secondSqlSessionFactory")
	public SqlSessionFactory setSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations( new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/kettle/*.xml"));// 设置mybatis的xml所在位置
		return bean.getObject();
	}

	@Bean(name = "secondSqlSessionTemplate")
	public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
