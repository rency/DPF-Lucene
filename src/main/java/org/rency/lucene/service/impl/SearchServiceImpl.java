package org.rency.lucene.service.impl;

import org.rency.lucene.beans.SearchResult;
import org.rency.lucene.core.Searcher;
import org.rency.lucene.service.SearchService;
import org.rency.utils.common.PageQueryResult;
import org.rency.utils.exceptions.CoreException;
import org.springframework.stereotype.Service;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Override
	public PageQueryResult<SearchResult> queryByKeyword(String keyword) throws CoreException {
		Searcher searcher = new Searcher();
		return searcher.search(keyword);
	}

	
	@Override
	public PageQueryResult<SearchResult> queryByKeyword(String keyword,int pageNo, int pageSize) throws CoreException {
		Searcher searcher = new Searcher();
		return searcher.search(keyword, pageNo, pageSize);
	}
}