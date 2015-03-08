package org.rency.lucene.service;

import org.rency.lucene.beans.SearchResult;
import org.rency.utils.common.PageQueryResult;
import org.rency.utils.exceptions.CoreException;

public interface SearchService {

	/**
	 * @desc 根据关键字搜索
	 * @date 2015年1月5日 上午10:06:09
	 * @param keyword 关键字
	 * @return
	 * @throws CoreException
	 */
	public PageQueryResult<SearchResult> queryByKeyword(String keyword) throws CoreException;
	
	/**
	 * @desc 根据关键字分页搜索
	 * @date 2015年1月7日 上午10:44:24
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws CoreException
	 */
	public PageQueryResult<SearchResult> queryByKeyword(String keyword,int pageNo,int pageSize) throws CoreException;
	
}