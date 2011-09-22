package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageListLineAdapter implements PageListLine {

    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String DATA_URL = "dataURL";
    private static final String CHECKBOX = "checkbox";
    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String ID_HEADER = "idHeader";
    private static final String ENTRIES_LINE = LabsSiteConstants.Schemas.PAGELIST_LINE.prefix() + ":entriesLine";
    
    protected DocumentModel doc;

    public PageListLineAdapter(DocumentModel doc) {
        this.doc = doc;
    }
    
    public static class Model {
        private DocumentModel doc;
        
        /**
         * PageListLine adapter = new PageListLineAdapter.Model(session, "/", "title").create();
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title) throws ClientException {
            this.doc = session.createDocumentModel(parentPath, title, Docs.PAGELIST_LINE.type());
        }
        
        /**
         * Creates document model in repository.
         * @return an adapter
         * @throws ClientException
         */
        public PageListLine create() throws ClientException {
            return new PageListLineAdapter(this.doc.getCoreSession().createDocument(this.doc));
        }
        
        /**
         * Getter an adapter
         * @return an adapter
         * @throws ClientException
         */
        public PageListLine getAdapter() throws ClientException{
            return new PageListLineAdapter(this.doc);
        }
    }
    
    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine#addLine(com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.EntriesLine)
     */
    @Override
    public void setLine(EntriesLine pLine) throws ClientException {
        Map<String, Object> map = null;
        List<Map<String, Object>> entriesList = new ArrayList<Map<String,Object>>();
        for (Entry entry:pLine.getEntries()){
            map = getEntryMap(entry);
            entriesList.add(map);
        }      
        doc.getProperty(ENTRIES_LINE).setValue(entriesList);
    }

    /**
     * Get the map of entry, the keys are the properties of entry
     * @param pEntry
     * @return the map of entry' properties
     */
    private Map<String, Object> getEntryMap(Entry pEntry) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_HEADER, pEntry.getIdHeader());
        map.put(TEXT, pEntry.getText());
        map.put(DATE, pEntry.getDate());
        map.put(CHECKBOX, pEntry.isCheckbox());
        map.put(DATA_URL, getUrlMap(pEntry.getUrl()));
        return map;
    }
    
    /**
     * Get the map of UrlType, the keys are the properties of UrlType
     * @param pUrl
     * @return the map of urlType' properties
     */
    private Map<String, Object> getUrlMap(UrlType pUrl) {
        if (pUrl == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(NAME, pUrl.getName());
        map.put(URL, pUrl.getUrl());
        return map;
    }

    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine#getLine()
     */
    @Override
    public EntriesLine getLine() throws ClientException {
        EntriesLine line = new EntriesLine();
        List<Entry> entries = line.getEntries();
        Serializable objEntriesList = doc.getPropertyValue(ENTRIES_LINE);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> entriesList = (List<Map<String, Object>>) objEntriesList;
        for (Map<String, Object> entry : entriesList){
            entries.add(getEntry(entry));
        }
        line.setEntries(entries);
        return line;
    }
    
    /**
     * Get the entry
     * @param mapEntry the nuxeo'xsd
     * @return a entry
     */
    @SuppressWarnings("unchecked")
    private Entry getEntry(Map<String, Object> mapEntry) {
        if (mapEntry == null){
            return null;
        }
        Entry entry  = new Entry();
        entry.setIdHeader(((Long)mapEntry.get(ID_HEADER)).intValue());
        entry.setText((String)mapEntry.get(TEXT));
        entry.setDate((Calendar)mapEntry.get(DATE));
        entry.setCheckbox((Boolean)mapEntry.get(CHECKBOX));
        entry.setUrl(getUrlType((Map<String, Object>)mapEntry.get(DATA_URL)));
        return entry;
    }
    
    /**
     * Get the urlType
     * @param pMap the nuxeo'xsd
     * @return a urlType
     */
    private UrlType getUrlType(Map<String, Object> pMap){
        if (pMap == null){
            return null;
        }
        return new UrlType((String)pMap.get(NAME), (String)pMap.get(URL));
    }

}
