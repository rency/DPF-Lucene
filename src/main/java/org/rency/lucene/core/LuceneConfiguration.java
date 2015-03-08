package org.rency.lucene.core;

/**
 * @desc 索引配置
 * @author T-rency
 * @date 2014年12月30日 上午11:13:22
 */
public class LuceneConfiguration {
	
	private volatile Thread indexThread;
	
	/**
	 * 索引储存方式 1-文件；2-MySQL
	 */
	private int storeType = 1;
	
	/**
	 * 如果储存方式为文件，则需要设置文件路径
	 */
	private String storePath;
	
	private String user;
	
	/**
	 * 初始化配置
	 * @param user 用户标识
	 * @param storeType 储存类型
	 * @param storePath 储存路径(仅储存类型为文件时有用)
	 */
	public LuceneConfiguration(String user,int storeType,String storePath){
		this.user = user;
		this.storeType = storeType;
		this.storePath = storePath;
	}

	public Thread getIndexThread() {
		return indexThread;
	}

	public void setIndexThread(Thread indexThread) {
		this.indexThread = indexThread;
	}

	public String getUser() {
		return user;
	}

	public int getStoreType(){
		return this.storeType;
	}
	
	public String getStorePath(){
		return this.storePath;
	}
}