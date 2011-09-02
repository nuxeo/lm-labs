package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageListEntryAdapter implements PageListEntry {

    private static final String DATA_DATA_URL = "pglen:dataURL";
    private static final String DATA_CHECKBOX = "pglen:checkbox";
    private static final String DATA_DATE = "pglen:date";
    private static final String DATA_TEXT = "pglen:text";
    private static final String DATA_ID_HEADER = "pglen:idHeader";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final int DEFAULT_INT = -1;
    public static final int NULL_VALUE_FOR_INT = DEFAULT_INT;
    
    protected DocumentModel doc;

    public PageListEntryAdapter(DocumentModel doc) {
        this.doc = doc;
    }
    
    public static class Model {
        private DocumentModel doc;
        
        /**
         * PageList adapter = new PageListAdapter.Model(session, "/", "title").create();
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title) throws ClientException {
            this.doc = session.createDocumentModel(parentPath, title, Docs.PAGELISTENTRY.type());
        }
        
        /**
         * Creates document model in repository.
         * @return an adapter
         * @throws ClientException
         */
        public PageListEntry create() throws ClientException {
            return new PageListEntryAdapter(this.doc.getCoreSession().createDocument(this.doc));
        }
        
        /**
         * Getter an adapter
         * @return an adapter
         * @throws ClientException
         */
        public PageListEntry getAdapter() throws ClientException{
            return new PageListEntryAdapter(this.doc);
        }
    }

    @Override
    public void setIdHeader(int idHeader) throws ClientException {
        doc.setPropertyValue(DATA_ID_HEADER, idHeader);
    }

    @Override
    public int getIdHeader() throws ClientException {
        Serializable value = doc.getPropertyValue(DATA_ID_HEADER);
        if (value != null && !StringUtils.isEmpty(value.toString())){
            return Integer.valueOf(value.toString()).intValue();
        }
        return DEFAULT_INT;
    }

    @Override
    public void setText(String text) throws ClientException {
        doc.setPropertyValue(DATA_TEXT, text);
    }

    @Override
    public Object getText() throws ClientException {
        return (String) doc.getPropertyValue(DATA_TEXT);
    }

    @Override
    public void setDate(Calendar cal) throws ClientException {
        doc.setPropertyValue(DATA_DATE, cal);
    }

    @Override
    public Calendar getDate() throws ClientException {
        return (Calendar) doc.getPropertyValue(DATA_DATE);
    }

    @Override
    public void setCheckbox(boolean checkbox) throws ClientException {
        doc.setPropertyValue(DATA_CHECKBOX, checkbox);
    }

    @Override
    public boolean getCheckBox() throws ClientException {
        Serializable value = doc.getPropertyValue(DATA_CHECKBOX);
        if (value != null && !StringUtils.isEmpty(value.toString())){
            return Boolean.valueOf(value.toString()).booleanValue();
        }
        return DEFAULT_BOOLEAN;
    }

    @Override
    public void setDataURL(UrlType url) throws ClientException {
        if (url == null){
            url = new UrlType(null, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", url.getName());
        map.put("url", url.getUrl());
        doc.getProperty(DATA_DATA_URL).setValue(map);
    }

    @Override
    public UrlType getDataURL() throws ClientException {
        Serializable value = doc.getPropertyValue(DATA_DATA_URL);
        if (value != null){
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            return new UrlType((String)map.get("name"), (String)map.get("url"));
        }
        return null;
    }
}
