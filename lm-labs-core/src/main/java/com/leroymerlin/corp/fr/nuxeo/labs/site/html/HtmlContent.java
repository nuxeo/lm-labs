package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HtmlContent {

	private final int colNumber;
	private final String html;

	public HtmlContent(int colNumber, String html) {
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

}
