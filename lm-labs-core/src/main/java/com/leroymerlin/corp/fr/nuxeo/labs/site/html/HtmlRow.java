package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlRow {

	private final HtmlSection parentSection;
	private Map<String, Serializable> rowMap = new HashMap<String, Serializable>();
	private List<HtmlContent> contents;

	public HtmlRow(HtmlSection parentSection) {
		this.parentSection = parentSection;
	}

	public HtmlRow(HtmlSection htmlSection, Map<String, Serializable> rowMap) {
		this.parentSection = htmlSection;
		this.rowMap = rowMap;
	}

	public Map<String, Serializable> toList() {
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		result.put("contents", (Serializable) contentsToMap());
		return result;
	}

	public void addContent(int colNumber, String html) {
		getContents().add(new HtmlContent(colNumber, html));
		parentSection.update();
	}

	private List<Map<String, Serializable>> contentsToMap() {
		List<Map<String, Serializable>> contentMap = new ArrayList<Map<String, Serializable>>();
		for (HtmlContent content : getContents()) {
			contentMap.add(content.toMap());
		}
		return contentMap;

	}

	public List<HtmlContent> getContents() {
		if (contents == null) {
			contents = new ArrayList<HtmlContent>();
			if(rowMap.containsKey("content")) {
				List<Map<String, Serializable>> contentsMap = (List<Map<String, Serializable>>) rowMap.get("content");
				for(Map<String,Serializable> map : contentsMap) {
					int colNumber = (Integer) map.get("colnumber");
					String html = (String) map.get("html");
					this.contents.add(new HtmlContent(colNumber,html));
				}
			}
		}
		return contents;
	}

}
