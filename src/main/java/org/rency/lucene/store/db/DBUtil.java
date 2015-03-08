package org.rency.lucene.store.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.jdbc.JdbcDirectory;
import org.rency.utils.common.SpringContextHolder;

public class DBUtil {

	public static DataSource getDataSource(){
		DataSource dataSource = SpringContextHolder.getBean(DataSource.class);
		if(dataSource == null){
			throw new IllegalArgumentException("the lucene property['dataSource'] can not be null.");
		}
		return dataSource;
	}
	
	public static Connection getConnection() throws SQLException{
		return getDataSource().getConnection();
	}
	
	public static void createIndexTable(Directory directory){
		JdbcDirectory jdbcDirectory = (JdbcDirectory) directory;
		try{
			if(!jdbcDirectory.tableExists()){
				jdbcDirectory.create();
			}
		}catch(IOException e){
			e.printStackTrace();
			throw new IllegalArgumentException("create lucene index table error."+e);
		}
	}
}