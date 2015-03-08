package org.rency.lucene.utils;

public class LuceneDict {

	/**
	 * 索引根目录
	 */
	public static final String INDEX_PATH = "D:/Lucene/index";
	
	/**
	 * 索引储存字段
	 */
	public static final String FIELD_URL = "URL";
	
	/**
	 * 索引储存字段
	 */
	public static final String FIELD_MODIFIED = "MODIFIED";

	/**
	 * 索引储存字段
	 */
	public static final String FIELD_CONTENT = "CONTENT";
	
	/**
	 * 索引储存字段
	 */
	public static final String FIELD_TITLE = "TITLE";
	
	/**
	 * 一次获取未索引记录数量
	 */
	public static final Integer GET_UNINDEX_RECORD_LIMIT = 1000;
	
	public static final String LUCENE_INDEX_TABLE_NAME = "t_lucene_index";
	
	/**
	 * 索引储存方式：文件
	 */
	public static final int LUCENE_INDEX_STORE_FILE = 1;
	
	/**
	 * 索引储存方式：MySQL
	 */
	public static final int LUCENE_INDEX_STORE_MYSQL = 2;
}