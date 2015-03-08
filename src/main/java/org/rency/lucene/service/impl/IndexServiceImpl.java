package org.rency.lucene.service.impl;

import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.lucene.dao.IndexDao;
import org.rency.lucene.service.IndexService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.exceptions.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImpl implements IndexService {
	
	@Autowired
	@Qualifier("indexDao")
	private IndexDao indexDao;

	@Override
	public List<WebPage> queryUnIndexPage() throws CoreException{
		return indexDao.findUnIndexPage(LuceneDict.GET_UNINDEX_RECORD_LIMIT);
	}

	@Override
	public int updateWebPageIndexStatus(WebPage page) throws CoreException {
		return indexDao.updateWebPageIndexStatus(page);
	}

	
	@Override
	public int updateAllIndexStatus(boolean status) throws CoreException {
		return indexDao.updateAllIndexStatus(status);
	}
	
}