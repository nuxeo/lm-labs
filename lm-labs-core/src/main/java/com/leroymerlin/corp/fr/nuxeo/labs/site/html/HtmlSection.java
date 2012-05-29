package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

public interface HtmlSection extends ChangeListener {

    public void setTitle(String title, CoreSession session) throws ClientException;

    public void setDescription(String description, CoreSession session) throws ClientException;

    public String getTitle() throws ClientException;

    public String getDescription() throws ClientException;

    public HtmlRow addRow(CoreSession session) throws ClientException;

    public List<HtmlRow> getRows(CoreSession session);

    public HtmlRow row(int index, CoreSession session);

    public HtmlSection insertBefore() throws ClientException;

    public void remove() throws ClientException;

    public HtmlRow insertBefore(HtmlRow htmlRow, CoreSession session) throws ClientException;

    public void remove(HtmlRow row, CoreSession session) throws ClientException;

    public HtmlRow addRow(String cssClass, CoreSession session) throws ClientException;

}
