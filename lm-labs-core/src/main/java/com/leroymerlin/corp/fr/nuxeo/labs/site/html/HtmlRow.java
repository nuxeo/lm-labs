package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsSessionImpl;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;

public class HtmlRow extends LabsSessionImpl {

    private static final Log LOG = LogFactory.getLog(HtmlRow.class);
    private static final String SCHEMA_NAME_DIRECTORY_COLUMNS_LAYOUT = Directories.COLUMNS_LAYOUT.schema();
    private static final String PROPERTY_SHORT_NAME_ORDER = Directories.COLUMNS_LAYOUT.orderingField();
    private static final String PROPERTY_NAME_COLUMNS_LAYOUT_LABEL = SCHEMA_NAME_DIRECTORY_COLUMNS_LAYOUT + ":label";
    private static final String PROPERTY_NAME_COLUMNS_LAYOUT_CODE = SCHEMA_NAME_DIRECTORY_COLUMNS_LAYOUT + ":" + Directories.COLUMNS_LAYOUT.idField();
    private static final String PROPERTY_NAME_COLUMNS_LAYOUT_SPANS = SCHEMA_NAME_DIRECTORY_COLUMNS_LAYOUT + ":spans";
//    private static final String PROPERTY_NAME_COLUMNS_LAYOUT_ORDER = SCHEMA_NAME_DIRECTORY_COLUMNS_LAYOUT + ":" + PROPERTY_SHORT_NAME_ORDER;
    private static final String DIRECTORY_NAME_COLUMNS_LAYOUT = Directories.COLUMNS_LAYOUT.dirName();

    private static final String CSS_PROPERTY_NAME = "cssclass";
    private static final String USER_PROPERTY_CLASS = "userclass";
    private static final String NO_CONTENT = "";
    private final HtmlSection parentSection;
    private Map<String, Serializable> rowMap = new HashMap<String, Serializable>();
    private List<HtmlContent> contents;
    private String cssClass;
    private List<String> userClass;

    public HtmlRow(HtmlSection parentSection) {
        this.parentSection = parentSection;
    }

    @SuppressWarnings("unchecked")
	public HtmlRow(HtmlSection htmlSection, Map<String, Serializable> rowMap) {
        this.parentSection = htmlSection;
        this.rowMap = rowMap;
        this.cssClass = (String) (rowMap.containsKey(CSS_PROPERTY_NAME) ? rowMap.get(CSS_PROPERTY_NAME) : null);
        this.userClass = (List<String>) (rowMap.containsKey(USER_PROPERTY_CLASS) ? rowMap.get(USER_PROPERTY_CLASS) : null);
    }

    public HtmlRow(HtmlSectionImpl htmlSection, String cssClass) {
        this.parentSection = htmlSection;
        this.cssClass = cssClass;
        this.userClass = new ArrayList<String>(0);
    }

    public HtmlRow(HtmlSectionImpl htmlSection, String cssClass, List<String> userClass) {
        this.parentSection = htmlSection;
        this.cssClass = cssClass;
        this.userClass = userClass;
    }

    public Map<String, Serializable> toMap() {
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        result.put("contents", (Serializable) contentsToListOfMap());
        result.put(CSS_PROPERTY_NAME, cssClass);
        result.put(USER_PROPERTY_CLASS, (Serializable) userClass);
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
                    String type = (String) map.get("type");
                    if (HtmlContent.Type.WIDGET_CONTAINER.type().equals(type)) {
                        @SuppressWarnings("unchecked")
                        List<String> refs = (List<String>) map.get("widgetRefs");
                        String html = (String) map.get("html");
                        this.contents.add(new HtmlContent(this, (int)colNumber, refs, html));
                    } else {
                        String html = (String) map.get("html");
                        this.contents.add(new HtmlContent(this, (int)colNumber, html));
                    }
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

	public void remove(CoreSession session) throws Exception {
    	LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
        for (HtmlContent content:this.getContents()){
            service.removeAllGadgetsOfHtmlContent(content, session);
        }
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

    public void setCssClass(String cssClass)  throws ClientException{
        this.cssClass = cssClass;
        this.update();
    }

    @SuppressWarnings("unchecked")
	public List<String> getUserClass() {
    	if (this.userClass == null){
    		this.userClass = new ArrayList<String>();
    		if(rowMap.containsKey(USER_PROPERTY_CLASS)) {
                this.userClass = (List<String>)rowMap.get(USER_PROPERTY_CLASS);
    		}
    	}
        return this.userClass;
    }

    public void setUserClass(List<String> userClass)  throws ClientException{
        this.userClass = userClass;
        this.update();
    }

    public void initTemplate(String templateName) throws ClientException {
        for (HtmlContent content : this.getContents()) {
            content.remove();
        }
        Session session = null;
        try {
            DirectoryService directoryService = Framework.getService(DirectoryService.class);
            session = directoryService.open(DIRECTORY_NAME_COLUMNS_LAYOUT);
            DocumentModel entry = session.getEntry(templateName);
            String[] spans = StringUtils.split((String) entry.getPropertyValue(PROPERTY_NAME_COLUMNS_LAYOUT_SPANS), "|");
            for (String span : spans) {
                this.addContent(Integer.parseInt(span), NO_CONTENT);
            }
        } catch (Exception e) {
            throw new ClientException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static Map<String, String> getColumnLayoutsSelect() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Session session = null;
        try {
            DirectoryService directoryService = Framework.getService(DirectoryService.class);
            session = directoryService.open(DIRECTORY_NAME_COLUMNS_LAYOUT);
            Map<String, String> orderBy = new LinkedHashMap<String, String>();
            Map<String, Serializable> filter = Collections.emptyMap();
            Set<String> fulltext = Collections.emptySet();
            orderBy.put(PROPERTY_SHORT_NAME_ORDER, "asc");
            for (DocumentModel entry : session.query(filter, fulltext, orderBy)) {
                map.put((String) entry.getPropertyValue(PROPERTY_NAME_COLUMNS_LAYOUT_CODE), (String) entry.getPropertyValue(PROPERTY_NAME_COLUMNS_LAYOUT_LABEL));
            }
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (DirectoryException e) {
                    LOG.error("Unable to close session: " + e.getMessage());
                }
            }
        }
        return map;
    }

}
