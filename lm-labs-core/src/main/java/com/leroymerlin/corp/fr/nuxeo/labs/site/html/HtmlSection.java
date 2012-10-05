package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

public interface HtmlSection extends ChangeListener, MovableElement {

    public void setTitle(String title) throws ClientException;

    public void setDescription(String description) throws ClientException;

    public String getTitle() throws ClientException;

    public String getDescription() throws ClientException;

    public HtmlRow addRow() throws ClientException;

    public List<HtmlRow> getRows();

    public HtmlRow row(int index);

    public HtmlSection insertBefore() throws ClientException;

    public void remove() throws ClientException;

    public HtmlRow insertBefore(HtmlRow htmlRow) throws ClientException;

    public void remove(HtmlRow row) throws ClientException;

    public HtmlRow addRow(String cssClass, List<String> userClass) throws ClientException;
    
    public Map<String, Serializable> toMap();

}
