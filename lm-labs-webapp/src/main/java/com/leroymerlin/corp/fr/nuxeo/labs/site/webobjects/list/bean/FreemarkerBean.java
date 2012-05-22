package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;

public class FreemarkerBean implements Serializable {

    private static final long serialVersionUID = 6533954658975698868L;
    
    String headersMapJS;
    
    String headersNameJS;
    
    Set<Header> headersSet;
    
    List<String> headersNameList;
    
    public FreemarkerBean(String headersMapJS, String headersNameJS, Set<Header> headersSetList, List<String> headersNameList) {
        this();
        this.headersMapJS = headersMapJS;
        this.headersNameJS = headersNameJS;
        this.headersSet = headersSetList;
        this.headersNameList = headersNameList;
    }

    private FreemarkerBean() {
        super();
    }

    public String getHeadersMapJS() {
        return headersMapJS;
    }

    public void setHeadersMapJS(String headersMapJS) {
        this.headersMapJS = headersMapJS;
    }

    public String getHeadersNameJS() {
        return headersNameJS;
    }

    public void setHeadersNameJS(String headersNameJS) {
        this.headersNameJS = headersNameJS;
    }

    public Set<Header> getHeadersSet() {
        return headersSet;
    }

    public void setHeadersSet(Set<Header> headersSet) {
        this.headersSet = headersSet;
    }

    public List<String> getHeadersNameList() {
        return headersNameList;
    }

    public void setHeadersNameList(List<String> headersNameList) {
        this.headersNameList = headersNameList;
    }

}
