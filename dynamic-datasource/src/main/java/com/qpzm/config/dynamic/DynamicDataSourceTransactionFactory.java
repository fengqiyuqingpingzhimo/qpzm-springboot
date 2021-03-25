package com.qpzm.config.dynamic;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**  
* @Description: 重写事务管理工厂
* @Author: 风起于青苹之末
* @Date: 2021年3月25日
*/
public class DynamicDataSourceTransactionFactory extends SpringManagedTransactionFactory {
	
	@Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new DynamicDataSourceTransaction(dataSource);
    }

}
