package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class PageListAdapter extends AbstractPage implements PageList {

    public static final String LINE_TITTLE = "lineTitle";
    private static final String PGL_HEADERLIST = LabsSiteConstants.Schemas.PAGELIST.prefix() + ":headerlist";
    private static final String ALL_CONTRIBUTORS = LabsSiteConstants.Schemas.PAGELIST.prefix() + ":allContributors";
    private static final String COMMENTABLE_LINES = LabsSiteConstants.Schemas.PAGELIST.prefix() + ":commentableLines";
    private static final String WIDTH = "width";
    private static final String ID_HEADER = "idHeader";
    private static final String FONT_SIZE = "fontSize";
    private static final String FONT_NAME = "fontName";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private static final String ORDER_POSITION = "orderPosition";
    private static final String SELECT_LIST = "selectlist";
    public static final int NULL_VALUE_FOR_INT = -1;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMM yyyy");

    public PageListAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public static class Model {
        private DocumentModel doc;

        /**
         * PageList adapter = new PageListAdapter.Model(session, "/",
         * "title").create();
         * 
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
         * 
         * @return an adapter
         * @throws ClientException
         */
        public PageList create() throws ClientException {
            return new PageListAdapter(this.doc.getCoreSession()
                    .createDocument(this.doc));
        }

        /**
         * Getter an adapter
         * 
         * @return an adapter
         * @throws ClientException
         */
        public PageList getAdapter() throws ClientException {
            return new PageListAdapter(this.doc);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#addHeader(com.
     * leroymerlin.corp.fr.nuxeo.labs.site.list.dto.Header)
     */
    @Override
    public void addHeader(Header pHead) throws ClientException {
        Map<String, Object> map = getHeaderMap(pHead);
        Serializable objHeaderList = doc.getPropertyValue(PGL_HEADERLIST);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> headerList = (List<Map<String, Object>>) objHeaderList;
        headerList.add(map);
        doc.getProperty(PGL_HEADERLIST)
                .setValue(headerList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#setHeaders(java
     * .util.List)
     */
    @Override
    public void setHeaders(List<Header> headersToSave) throws ClientException {
        List<Map<String, Object>> listHeaders = new ArrayList<Map<String, Object>>();
        for (Header head : headersToSave) {
            listHeaders.add(getHeaderMap(head));
        }
        doc.getProperty(PGL_HEADERLIST)
                .setValue(listHeaders);
    }

    /**
     * Get the map of header, the keys are the properties of header
     * 
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
        map.put(ORDER_POSITION, pHead.getOrderPosition());
        map.put(SELECT_LIST, pHead.getSelectlist());
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#getHeaderlist()
     */
    @Override
    public SortedSet<Header> getHeaderSet() throws ClientException {
        SortedSet<Header> headerlist = new TreeSet<Header>();
        Serializable objHeaderList = doc.getProperty(PGL_HEADERLIST);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> headerList = (List<Map<String, Object>>) objHeaderList;
        for (Map<String, Object> list : headerList) {
            headerlist.add(getHeader(list));
        }
        return headerlist;
    }

    /**
     * Create the header with a map of property and value
     * 
     * @param pMap
     * @throws PropertyException
     * @throws NullException
     */
    private Header getHeader(Map<String, Object> pMap) throws PropertyException {
        if (pMap == null) {
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
            header.setOrderPosition(Tools.getInt(pMap.get(ORDER_POSITION)));
        } catch (NullException e) {
            throw new IllegalArgumentException("This object is null.", e);
        }
        header.setFontSize(Tools.getString(pMap.get(FONT_SIZE)));
        header.setWidth(Tools.getString(pMap.get(WIDTH)));
        header.setSelectlist(Tools.getStringList(pMap.get(SELECT_LIST)));
        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#resetHeaders()
     */
    @Override
    public void resetHeaders() throws ClientException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        doc.getProperty(PGL_HEADERLIST)
                .setValue(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#getLines()
     */
    @Override
    public List<EntriesLine> getLines() throws ClientException {
        DocumentModelList listDocLines = null;
        List<EntriesLine> entriesLines = new ArrayList<EntriesLine>();
        listDocLines = doc.getCoreSession()
                .getChildren(doc.getRef(), LabsSiteConstants.Docs.PAGELIST_LINE.type());
        for (DocumentModel docTmp : listDocLines) {
            EntriesLine line = docTmp.getAdapter(PageListLine.class)
                    .getLine();
            line.setDocLine(docTmp);
            entriesLines.add(line);
        }
        return entriesLines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#saveLine(com.
     * leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine)
     */
    @Override
    public void saveLine(EntriesLine pLine) throws ClientException {
        CoreSession session = doc.getCoreSession();
        DocumentModel lineDoc = null;
        boolean isNew = pLine.getDocLine() == null;
        if (isNew) {
            lineDoc = session.createDocumentModel(doc.getPathAsString(), LINE_TITTLE, LabsSiteConstants.Docs.PAGELIST_LINE.type());
        } else {
            lineDoc = pLine.getDocLine();
        }
        PageListLine line = lineDoc.getAdapter(PageListLine.class);
        line.setLine(pLine);
        if (isNew) {
            lineDoc = session.createDocument(lineDoc);
        } else {
            lineDoc = session.saveDocument(lineDoc);
        }
        session.save();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#removeLine(org.
     * nuxeo.ecm.core.api.DocumentRef)
     */
    @Override
    public void removeLine(DocumentRef pRef) throws ClientException {
        doc.getCoreSession()
                .removeDocument(pRef);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#getLine(org.nuxeo
     * .ecm.core.api.DocumentRef)
     */
    @Override
    public EntriesLine getLine(DocumentRef pRef) throws ClientException {
        EntriesLine line = new EntriesLine();
        DocumentModel lineDoc = doc.getCoreSession()
                .getDocument(pRef);
        if (lineDoc != null) {
            line.setDocLine(lineDoc);
            line = lineDoc.getAdapter(PageListLine.class)
                    .getLine();
        }
        return line;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#isAllCintibutors()
     */
    @Override
    public boolean isAllContributors() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue(ALL_CONTRIBUTORS);
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#setAllContributors
     * (boolean)
     */
    @Override
    public void setAllContributors(boolean isAllContributors) throws ClientException {
        doc.setPropertyValue(ALL_CONTRIBUTORS, isAllContributors);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#isCommentableLines
     * ()
     */
    @Override
    public boolean isCommentableLines() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue(COMMENTABLE_LINES);
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#setCommentableLines
     * (boolean)
     */
    @Override
    public void setCommentableLines(boolean isAllCommentablesLines) throws ClientException {
        doc.setPropertyValue(COMMENTABLE_LINES, isAllCommentablesLines);
    }

    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList#exportExcel(java.io.OutputStream)
     */
    @Override
    public void exportExcel(OutputStream pOut) throws ClientException, IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("export");

        int numRow = 0;
        int numCell = 0;
        Cell cell = null;
        Row row = null;
        SortedSet<Header> headers = getHeaderSet();
        
        Font titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        CellStyle style = wb.createCellStyle();
        style.setFont(titleFont);
        
        Row headerRow = sheet.createRow(numRow);
        for (Header head : headers) {
            cell = headerRow.createCell(numCell);
            cell.setCellStyle(style);
            cell.setCellValue(head.getName());
            numCell ++;
        }
        
        numCell = 0;
        numRow ++;
        for (EntriesLine line : getLines()) {
            row = sheet.createRow(numRow);
            for (Header head : headers) {
                cell = row.createCell(numCell);
                Entry entry = line.getEntryByIdHead(head.getIdHeader());
                switch (EntryType.valueOf(head.getType())) {
                case CHECKBOX:
                    if (entry.isCheckbox()){
                        cell.setCellValue("OUI");
                    }
                    else{
                        cell.setCellValue("NON");
                    }
                    break;
                case DATE:
                    cell.setCellValue(sdf.format(entry.getDate().getTime()));
                    break;
                case SELECT:
                    cell.setCellValue(entry.getText());
                    break;
                case TEXT:
                    cell.setCellValue(entry.getText());
                    break;
                case URL:
                    if (entry.getUrl() != null) {
                        cell.setCellValue(entry.getUrl()
                                .getUrl());
                    } else {
                        cell.setCellValue("");
                    }
                    break;
                default:
                    cell.setCellValue("");
                    break;
                }

                numCell++;
            }
            numCell = 0;
            numRow++;
        }
        wb.write(pOut);
    }

}
