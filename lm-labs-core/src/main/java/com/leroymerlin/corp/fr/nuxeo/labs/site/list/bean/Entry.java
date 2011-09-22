package com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean;

import java.util.Calendar;

public class Entry {
    
    private int idHeader;
    
    private String text;
    
    private Calendar date;

    private boolean checkbox;
    
    private UrlType url;
    

    public Entry(int idHeader, String text, Calendar date, boolean checkbox, UrlType url) {
        super();
        this.idHeader = idHeader;
        this.text = text;
        this.date = date;
        this.checkbox = checkbox;
        this.url = url;
    }
    
    public Entry() {
        super();
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public UrlType getUrl() {
        return url;
    }

    public void setUrl(UrlType url) {
        this.url = url;
    }
}
