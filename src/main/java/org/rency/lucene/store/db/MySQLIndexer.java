package org.rency.lucene.store.db;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.jdbc.JdbcDirectory;
import org.apache.lucene.store.jdbc.JdbcDirectorySettings;
import org.apache.lucene.store.jdbc.dialect.MySQLDialect;
import org.apache.lucene.store.jdbc.lock.NoOpLock;
import org.apache.lucene.util.Version;
import org.rency.crawler.beans.WebPage;
import org.rency.lucene.service.IndexService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLIndexer {
	
	private static final Logger logger = LoggerFactory.getLogger(MySQLIndexer.class);
	private IndexService indexService = SpringContextHolder.getBean(IndexService.class);

	public void index() throws CoreException {
		boolean create = true;
		try{
			JdbcDirectorySettings settings = new JdbcDirectorySettings();
			settings.setLockClass(NoOpLock.class);
			JdbcDirectory jdbcDir = new JdbcDirectory(DBUtil.getDataSource(),new MySQLDialect(),settings,LuceneDict.LUCENE_INDEX_TABLE_NAME);
			DBUtil.createIndexTable(jdbcDir);
			
			List<WebPage> pageList = null;
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2,analyzer);
			if(create){
				iwc.setOpenMode(OpenMode.CREATE);
			}else{
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			IndexWriter writer = new IndexWriter(jdbcDir,iwc);
			while( (pageList = indexService.queryUnIndexPage()) != null && (pageList = indexService.queryUnIndexPage()).size() > 0  ){
				for(WebPage page : pageList){
					indexDocs(writer,page);
					//修改索引状态为true
					page.setCreateIndex(true);
					indexService.updateWebPageIndexStatus(page);
				}
				writer.forceMerge(1);
				writer.close();
			}
			jdbcDir.close();
		}catch(IOException e){
			logger.error("添加索引异常.",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	private void indexDocs(IndexWriter writer,WebPage page) throws CoreException{
		logger.debug("Indexing Resources["+page.toString()+"]...");
		try{
			Document doc = new Document();
			Field pathField = new StringField(LuceneDict.FIELD_URL,page.getUrl(),Field.Store.YES);
			doc.add(pathField);
			doc.add(new LongField(LuceneDict.FIELD_MODIFIED,new Date().getTime(),Field.Store.NO));
			doc.add(new TextField(LuceneDict.FIELD_CONTENT,page.getTitle()+page.getKeywords()+page.getDescription()+page.getContent(),Field.Store.NO));
			if(writer.getConfig().getOpenMode() == OpenMode.CREATE){
				logger.debug("Adding "+page.toString());
				writer.addDocument(doc);
			}else{
				logger.debug("Updateing "+page.toString());
				writer.updateDocument(new Term(LuceneDict.FIELD_URL,page.getUrl()), doc);
			}
			writer.commit();
		}catch(Exception e){
			logger.error("添加索引异常.",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
}