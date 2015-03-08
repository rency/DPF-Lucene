package org.rency.lucene.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.rency.lucene.beans.SearchResult;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.common.PageQueryResult;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Searcher {

	private static final Logger logger = LoggerFactory.getLogger(Searcher.class);
	
	public PageQueryResult<SearchResult> search(String keywords) throws CoreException{
		logger.debug("start searcher for:["+keywords+"]");
		PageQueryResult<SearchResult> pqr = new PageQueryResult<SearchResult>();
		try {
			List<SearchResult> searchList = new ArrayList<SearchResult>();
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(LuceneDict.INDEX_PATH)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser(LuceneDict.FIELD_CONTENT,analyzer);
			Query query = parser.parse(keywords);
			TopDocs results = searcher.search(query,10);
			for(int i=0;i<results.scoreDocs.length;i++){
				SearchResult sr = new SearchResult();
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				sr.setUrl(doc.getField(LuceneDict.FIELD_URL).stringValue());
				sr.setContent(doc.getField(LuceneDict.FIELD_CONTENT).stringValue());
				sr.setTitle(doc.getField(LuceneDict.FIELD_TITLE).stringValue());
				
				long modify = Long.parseLong(doc.getField(LuceneDict.FIELD_MODIFIED).stringValue());
				Date modifyDate = new Date();
				modifyDate.setTime(modify);
				sr.setModifyDate(modifyDate);
				searchList.add(sr);
			}
			reader.close();
			pqr.setList(searchList);
			pqr.setTotalCount(searchList.size());
		} catch (IOException e) {
			logger.error("搜索时打开索引目录异常.",e);
			e.printStackTrace();
			throw new CoreException("搜索时打开索引目录异常."+e);
		} catch (ParseException e) {
			logger.error("搜索解析异常.",e);
			e.printStackTrace();
			throw new CoreException("搜索解析异常."+e);
		}
		logger.debug("finish searcher for:["+keywords+"],result size:"+pqr.getTotalCount());
		return pqr;
	}
	
	public PageQueryResult<SearchResult> search(String keywords,int pageNo,int pageSize) throws CoreException{
		logger.debug("start searcher for:["+keywords+"],pageNo["+pageNo+"],pageSize["+pageSize+"]");
		PageQueryResult<SearchResult> pqr = new PageQueryResult<SearchResult>();
		try {
			
			List<SearchResult> searchList = new ArrayList<SearchResult>();
			
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(LuceneDict.INDEX_PATH)));
			IndexSearcher searcher = new IndexSearcher(reader);
			
			Analyzer analyzer = new StandardAnalyzer();
			
			QueryParser parser = new QueryParser(LuceneDict.FIELD_CONTENT,analyzer);
			Query query = parser.parse(keywords);
			
			TopDocs topDocs = searcher.search(query,10000,new Sort(new SortField(LuceneDict.FIELD_MODIFIED,Type.LONG)));//返回前N条记录
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			int totalCount = topDocs.totalHits;
			int begin = pageSize*(pageNo - 1);
			int end = Math.min(begin+pageSize,scoreDocs.length);
			for(int i=begin;i<end;i++){
				SearchResult sr = new SearchResult();
				Document doc = searcher.doc(scoreDocs[i].doc);
				sr.setUrl(doc.getField(LuceneDict.FIELD_URL).stringValue());
				sr.setContent(doc.getField(LuceneDict.FIELD_CONTENT).stringValue());
				sr.setTitle(doc.getField(LuceneDict.FIELD_TITLE).stringValue());
				
				long modify = Long.parseLong(doc.getField(LuceneDict.FIELD_MODIFIED).stringValue());
				Date modifyDate = new Date();
				modifyDate.setTime(modify);
				sr.setModifyDate(modifyDate);
				searchList.add(sr);
			}
			reader.close();
			pqr.setList(searchList);
			pqr.setPageNo(pageNo);
			pqr.setPageSize(pageSize);
			pqr.setTotalCount(totalCount);
		} catch (IOException e) {
			logger.error("搜索时打开索引目录异常.",e);
			e.printStackTrace();
			throw new CoreException("搜索时打开索引目录异常."+e);
		} catch (ParseException e) {
			logger.error("搜索解析异常.",e);
			e.printStackTrace();
			throw new CoreException("搜索解析异常."+e);
		}
		logger.debug("finish searcher for:["+keywords+"],pageNo["+pageNo+"],pageSize["+pageSize+"]. total record:["+pqr.getTotalCount()+"]");
		return pqr;
	}
}
