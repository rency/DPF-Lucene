package org.rency.lucene.service;

import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.utils.exceptions.CoreException;

public interface IndexService {

	/**
	 * @desc 获取未索引的数据
	 * @date 2014年12月30日 下午1:45:04
	 * @return
	 * @throws CoreException
	 */
	public List<WebPage> queryUnIndexPage()throws CoreException;
	
	/**
	 * @desc 修改索引状态
	 * @date 2014年12月30日 下午3:06:46
	 * @param page
	 * @return
	 * @throws CoreException
	 */
	public int updateWebPageIndexStatus(WebPage page)throws CoreException;
	
	/**
	 * @desc 修改所有的索引状态
	 * @date 2015年1月6日 上午9:40:32
	 * @param status
	 * @return
	 * @throws CoreException
	 */
	public int updateAllIndexStatus(boolean status)throws CoreException;

}