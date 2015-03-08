package org.rency.lucene.service.impl;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.rency.lucene.service.IndexService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.exceptions.CoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class IndexServiceImplTest {
	
	private IndexService indexService;

	@Before
	public void before(){
		@SuppressWarnings("resource")
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/applicationContext.xml");
		indexService = ctx.getBean(IndexService.class);
	}
	
	@Test
	public void testInitWebPage() throws CoreException{
		File indexFile = new File(LuceneDict.INDEX_PATH);
		if(indexFile.exists()){
			indexFile.delete();
		}
		int count = indexService.updateAllIndexStatus(false);
		System.out.println("update webpage index status succ."+count);
		
	}

}
