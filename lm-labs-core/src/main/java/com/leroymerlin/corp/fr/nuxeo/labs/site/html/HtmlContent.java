package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

public class HtmlContent {

	private final HtmlRow parent;
	private int colNumber;
	private String html;

	public HtmlContent(HtmlRow parent, int colNumber, String html) {
		this.parent = parent;
		this.colNumber = colNumber;
		this.html = html;
	}

	public Integer getColNumber() {
		return colNumber;
	}

	public String getHtml() {
		return html;
	}

	public Map<String, Serializable> toMap() {
		Map<String,Serializable> map = new HashMap<String, Serializable>();
		map.put("colnumber", colNumber);
		map.put("html", html);
		return map;
	}

	public HtmlContent insertBefore(int i, String html) {
		return parent.insertContentBefore(this,i,html);
		
	}

	public void remove() {
		parent.removeContent(this);
		
	}

	public void setHtml(String html) throws ClientException {
		this.html = html;
		parent.update();
		
	}

	public void setColNumber(int colNumber) throws ClientException {
		this.colNumber = colNumber;
		parent.update();
		
	}

}
