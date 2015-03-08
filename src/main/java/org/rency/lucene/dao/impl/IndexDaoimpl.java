package org.rency.lucene.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.lucene.dao.IndexDao;
import org.rency.utils.dao.BasicDao;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("indexDao")
public class IndexDaoimpl implements IndexDao {

	private static final Logger logger = LoggerFactory.getLogger(IndexDaoimpl.class);
	
	@Autowired
	@Qualifier("jdbcDao")
	private BasicDao basicDao;
	
	@Override
	public List<WebPage> findUnIndexPage(int limit) throws CoreException {
		List<WebPage> pageList = new ArrayList<WebPage>();
		String queryString = "select model.url,model.content,model.contentMD5,model.description,model.keywords,model.title from t_crawler_web_page as model where model.createIndex=? order by model.execDate limit "+limit;
		try{
			ResultSet rs = basicDao.find(queryString, new Object[]{false});
			while(rs.next()){
				WebPage webPage = new WebPage();
				webPage.setUrl(rs.getString("url"));
				webPage.setContent(rs.getString("content"));
				webPage.setContentMD5(rs.getString("contentMD5"));
				webPage.setDescription(rs.getString("description"));
				webPage.setKeywords(rs.getString("keywords"));
				webPage.setTitle(rs.getString("title"));
				pageList.add(webPage);
			}
			return pageList;
		}catch(SQLException e){
			logger.error("获取未索引数据失败["+queryString+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}

	@Override
	public int updateWebPageIndexStatus(WebPage page) throws CoreException {
		String queryString = "update t_crawler_web_page as model set model.createIndex=? where model.url=? and model.contentMD5=? ";
		try{
			int count = basicDao.update(queryString, new Object[]{page.isCreateIndex(),page.getUrl(),page.getContentMD5()});
			return count;
		}catch(CoreException e){
			logger.error("更新索引状态失败["+queryString+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	

	@Override
	public int updateAllIndexStatus(boolean status) throws CoreException {
		String queryString = "update t_crawler_web_page as model set model.createIndex=?";
		try{
			int count = basicDao.update(queryString, new Object[]{status});
			return count;
		}catch(CoreException e){
			logger.error("更新所有索引状态失败["+queryString+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}

}
