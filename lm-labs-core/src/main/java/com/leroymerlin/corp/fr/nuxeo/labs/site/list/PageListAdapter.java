package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class PageListAdapter extends AbstractPage implements PageList {

    private static final String PGL_HEADERLIST = "pgl:headerlist";
    private static final String WIDTH = "width";
    private static final String ID_HEADER = "idHeader";
    private static final String FONT_SIZE = "fontSize";
    private static final String FONT_NAME = "fontName";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    public static final int NULL_VALUE_FOR_INT = -1;

    public PageListAdapter(DocumentModel doc) {
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
            this.doc = session.createDocumentModel(parentPath, title, Docs.PAGELIST.type());
        }
        
        /**
         * Creates document model in repository.
         * @return an adapter
         * @throws ClientException
         */
        public PageList create() throws ClientException {
            return new PageListAdapter(this.doc.getCoreSession().createDocument(this.doc));
        }
        
        /**
         * Getter an adapter
         * @return an adapter
         * @throws ClientException
         */
        public PageList getAdapter() throws ClientException{
            return new PageListAdapter(this.doc);
        }
    }
    
    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#addHeader(com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.Header)
     */
    @Override
    public void addHeader(Header pHead) throws ClientException {
        Map<String, Object> map = getHeaderMap(pHead);
        Serializable objHeaderList = doc.getPropertyValue(PGL_HEADERLIST);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> headerList = (List<Map<String, Object>>) objHeaderList;
        headerList.add(map);
        doc.getProperty(PGL_HEADERLIST).setValue(headerList);
    }

    /**
     * Get the map of header, the keys are the properties of header
     * @param pHead
     * @return the map of header
     */
    private Map<String, Object> getHeaderMap(Header pHead) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(NAME, pHead.getName());
        map.put(TYPE, pHead.getType());
        map.put(FONT_NAME, pHead.getFontName());
        map.put(FONT_SIZE, pHead.getFontSize());
        map.put(ID_HEADER, pHead.getIdHeader());
        map.put(WIDTH, pHead.getWidth());
        return map;
    }

    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#getHeaderlist()
     */
    @Override
    public SortedSet<Header> getHeaderSet() throws ClientException {
        SortedSet<Header> headerlist = new TreeSet<Header>();
        Serializable objHeaderList = doc.getProperty(PGL_HEADERLIST);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> headerList = (List<Map<String, Object>>) objHeaderList;
        for (Map<String, Object> list:headerList){
            headerlist.add(getHeader(list));
        }
        return headerlist;
    }
    
    /**
     * Create the header with a map of property and value
     * @param pMap
     * @throws PropertyException 
     * @throws NullException 
     */
    private Header getHeader(Map<String, Object> pMap) throws PropertyException{
        if (pMap == null){
            return null;
        }
        Header header = new Header();
        header.setName(Tools.getString(pMap.get(NAME)));
        header.setType(Tools.getString(pMap.get(TYPE)));
        header.setFontName(Tools.getString(pMap.get(FONT_NAME)));
        try {
            header.setIdHeader(Tools.getInt(pMap.get(ID_HEADER)));
        } catch (NullException e) {
            throw new IllegalArgumentException("This object is null.", e);
        }
        try {
            header.setFontSize(Tools.getInt(pMap.get(FONT_SIZE)));
        } catch (NullException e) {
            header.setFontSize(NULL_VALUE_FOR_INT);
        }
        try {
            header.setWidth(Tools.getInt(pMap.get(WIDTH)));
        } catch (NullException e) {
            header.setWidth(NULL_VALUE_FOR_INT);
        }
        return header;
    }
}
