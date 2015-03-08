package org.rency.lucene.service;

import org.rency.lucene.core.LuceneConfiguration;
import org.rency.utils.exceptions.CoreException;

public interface LuceneService {

	/**
	 * @desc 开始创建索引
	 * @date 2014年12月30日 下午3:42:11
	 * @param cfg
	 * @return
	 * @throws CoreException
	 */
	public boolean startIndex(LuceneConfiguration cfg) throws CoreException;
	
	/**
	 * @desc 停止索引
	 * @date 2014年12月30日 下午3:42:21
	 * @param cfg
	 * @return
	 * @throws CoreException
	 */
	public boolean stopIndex(LuceneConfiguration cfg) throws CoreException;
	
	/**
	 * @desc 查看索引状态
	 * @date 2014年12月30日 下午3:42:29
	 * @param cfg
	 * @return
	 * @throws CoreException
	 */
	public boolean statusIndex(LuceneConfiguration cfg) throws CoreException;
}