package com.qpzm.config.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**  
* @Description: springboot动态数据源 线程守护变量等
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
public class DynamicDataSource extends AbstractRoutingDataSource {

	//线程db标识变量
	private static final ThreadLocal<String> dbkey = new ThreadLocal<String>();

	private static DynamicDataSource instance;
	private static byte[] lock=new byte[0];
	private static Map<Object,Object> dataSourceMap=new HashMap<Object, Object>();
	
	@Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        super.afterPropertiesSet();// 必须添加该句，否则新添加数据源无法识别到
    }
 
    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }
	
	@Override
	protected Object determineCurrentLookupKey() {
		return getDBKey();
	}
	
	public static void setDBKey(String dataSourceKey) {
		dbkey.set(dataSourceKey);
	}
 
	public static String getDBKey() {
		return dbkey.get();
	}
 
	public static void clearDBKey() {
		dbkey.remove();
	}
 
	private DynamicDataSource() {}
	
	public static synchronized DynamicDataSource getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new DynamicDataSource();
                }
            }
        }
        return instance;
    }

}
