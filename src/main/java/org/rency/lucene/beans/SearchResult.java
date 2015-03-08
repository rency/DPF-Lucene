package org.rency.lucene.beans;

import java.io.Serializable;
import java.util.Date;

public class SearchResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8574793468061484469L;
	
	private int priority;
	private String url;
	private String title;
	private String content;
	private Date modifyDate;
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public String toString(){
		return "{url:"+url+", priority:"+priority+", title:"+title+", content:"+content+", modifyDate:"+modifyDate+"}";
	}
	
}