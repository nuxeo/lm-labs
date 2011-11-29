package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

public class HtmlRow {

    private static final String CSS_PROPERTY_NAME = "cssclass";
    private final HtmlSection parentSection;
    private Map<String, Serializable> rowMap = new HashMap<String, Serializable>();
    private List<HtmlContent> contents;
    private String cssClass;

    public HtmlRow(HtmlSection parentSection) {
        this.parentSection = parentSection;
    }

    public HtmlRow(HtmlSection htmlSection, Map<String, Serializable> rowMap) {
        this.parentSection = htmlSection;
        this.rowMap = rowMap;
        this.cssClass = (String) (rowMap.containsKey(CSS_PROPERTY_NAME) ? rowMap.get(CSS_PROPERTY_NAME) : null);
    }

    public HtmlRow(HtmlSectionImpl htmlSection, String cssClass) {
        this.parentSection = htmlSection;
        this.cssClass = cssClass;
    }

    public Map<String, Serializable> toMap() {
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        result.put("contents", (Serializable) contentsToListOfMap());
        result.put(CSS_PROPERTY_NAME, cssClass);
        return result;
    }

    public void addContent(int colNumber, String html) throws ClientException {
        getContents().add(new HtmlContent(this, colNumber, html));
        parentSection.onChange(this);
    }

    private List<Map<String, Serializable>> contentsToListOfMap() {
        List<Map<String, Serializable>> contentMap = new ArrayList<Map<String, Serializable>>();
        for (HtmlContent content : getContents()) {
            contentMap.add(content.toMap());
        }
        return contentMap;

    }

    public List<HtmlContent> getContents() {
        if (contents == null) {
            contents = new ArrayList<HtmlContent>();
            if(rowMap.containsKey("contents")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Serializable>> contentsMap = (List<Map<String, Serializable>>) rowMap.get("contents");
                for(Map<String,Serializable> map : contentsMap) {
                    long colNumber = (Long) map.get("colnumber");
                    String html = (String) map.get("html");
                    this.contents.add(new HtmlContent(this, (int)colNumber,html));
                }
            }
        }
        return contents;
    }

    public HtmlContent content(int index) {
        return getContents().get(index);
    }

    public HtmlRow insertBefore() throws ClientException {
        return parentSection.insertBefore(this);
    }

    public void remove() throws ClientException {
        parentSection.remove(this);

    }

    public HtmlContent insertContentBefore(HtmlContent content, int colNumber, String html) {
        List<HtmlContent> contents = getContents();
        HtmlContent newContent = new HtmlContent(this, colNumber, html);
        contents.add(contents.indexOf(content), newContent);
        return newContent;
    }

    public void removeContent(HtmlContent htmlContent) {
        getContents().remove(htmlContent);
    }

    public void update() throws ClientException {
        parentSection.onChange(this);

    }

    public String getCssClass() {
        return cssClass;
    }

}
