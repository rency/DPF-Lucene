package org.rency.lucene.core;

import java.io.File;
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
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.rency.crawler.beans.WebPage;
import org.rency.lucene.service.IndexService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.pushlet.beans.MessageQueue;
import org.rency.pushlet.service.MessageQueueService;
import org.rency.utils.common.CONST;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Indexer implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(Indexer.class);
	private MessageQueueService messageQueueService = SpringContextHolder.getBean(MessageQueueService.class);
	private IndexService indexService = SpringContextHolder.getBean(IndexService.class);
	private LuceneConfiguration cfg;
	private Directory directory;
	
	public Indexer(LuceneConfiguration cfg){
		this.cfg = cfg;
	}

	@Override
	public void run() {
		try {
			logger.info("开始创建索引于"+Utils.getNowDateTime()+", 索引储存方式为:"+cfg.getStoreType()+", 索引储存路径为:"+cfg.getStorePath());
			if(cfg.getStoreType() == LuceneDict.LUCENE_INDEX_STORE_FILE){//文件储存
				directory = FSDirectory.open(new File(cfg.getStorePath()));
				indexFile();
			}else if(cfg.getStoreType() == LuceneDict.LUCENE_INDEX_STORE_MYSQL){//MySQL储存
				throw new IllegalArgumentException("Lucene store index type is unimplement, and please select other store type."+cfg.getStoreType());
			}else{
				throw new IllegalArgumentException("Lucene store index type is unimplement, and please select other store type."+cfg.getStoreType());
			}
			messageQueueService.add(new MessageQueue(CONST.SERVICE_KIND_LUCENE, cfg.getUser(), "索引创建已完成"));
			logger.info("完成创建索引于"+Utils.getNowDateTime());
		} catch (Exception e) {
			logger.error("创建索引发生异常.",e);
			e.printStackTrace();
		} 
	}
	
	private void indexFile() throws CoreException{
		boolean create = true;
		try{
			List<WebPage> pageList = null;
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2,analyzer);
			if(create){
				iwc.setOpenMode(OpenMode.CREATE);
			}else{
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			IndexWriter writer = new IndexWriter(directory,iwc);
			while( (pageList = indexService.queryUnIndexPage()) != null && (pageList = indexService.queryUnIndexPage()).size() > 0  ){
				for(WebPage page : pageList){
					indexDocs(writer,page);
					//修改索引状态为true
					page.setCreateIndex(true);
					indexService.updateWebPageIndexStatus(page);
				}
			}
			writer.forceMerge(1);
			writer.close();
			directory.close();
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
			Field urlField = new StringField(LuceneDict.FIELD_URL,page.getUrl(),Field.Store.YES);
			doc.add(urlField);
			
			doc.add(new LongField(LuceneDict.FIELD_MODIFIED,new Date().getTime(),Field.Store.YES));
			
			doc.add(new TextField(LuceneDict.FIELD_CONTENT,page.getTitle()+page.getKeywords()+page.getDescription()+page.getContent(),Field.Store.YES));
			
			Field titleField = new StringField(LuceneDict.FIELD_TITLE,page.getTitle(),Field.Store.YES);
			doc.add(titleField);
			
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
