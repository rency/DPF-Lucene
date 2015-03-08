package org.rency.lucene.service.impl;

import org.rency.lucene.core.Indexer;
import org.rency.lucene.core.LuceneConfiguration;
import org.rency.lucene.service.LuceneService;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("luceneService")
public class LuceneServiceImpl implements LuceneService {
	
	private static final Logger logger = LoggerFactory.getLogger(LuceneServiceImpl.class);

	@Override
	public boolean startIndex(LuceneConfiguration cfg) throws CoreException {
		try{
			cfg.setIndexThread(new Thread(new Indexer(cfg)));
			cfg.getIndexThread().start();
			return true;
		}catch(Exception e){
			logger.error("创建索引启动异常",e);
			e.printStackTrace();
			throw new CoreException(e);
			
		}
	}

	@Override
	public boolean stopIndex(LuceneConfiguration cfg) throws CoreException {
		cfg.getIndexThread().interrupt();
		return true;
	}

	@Override
	public boolean statusIndex(LuceneConfiguration cfg) throws CoreException {
		return false;
	}

}
