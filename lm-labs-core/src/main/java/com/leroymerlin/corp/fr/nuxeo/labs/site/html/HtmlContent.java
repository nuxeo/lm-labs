package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsHtmlWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsOpensocialGadget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget;

public class HtmlContent {

    private static final Log LOG = LogFactory.getLog(HtmlContent.class);

    public enum Type {

        HTML("html"), WIDGET_CONTAINER("widgetcontainer");

        private String type;

        private static final Map<String, Type> stringToEnum = new HashMap<String, Type>();
        static { // Initialize map from constant name to enum constant
            for (Type op : values())
                stringToEnum.put(op.type(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static Type fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        Type(String type) {
            this.type = type;
        }

        public String type() {
            return type;
        }
    }

	private final HtmlRow parent;
	private int colNumber;
    private String html;
    private String type;
    private List<String> widgetRefs = new ArrayList<String>();
    private Map<String, LabsWidget> widgets = new LinkedHashMap<String, LabsWidget>();

	public HtmlContent(HtmlRow parent, int colNumber, String html) {
		this.parent = parent;
		this.colNumber = colNumber;
        this.type = Type.HTML.type();
		this.html = html;
	}

    public HtmlContent(HtmlRow parent, int colNumber, List<String> widgetRefs) {
        this.parent = parent;
        this.colNumber = colNumber;
        this.type = Type.WIDGET_CONTAINER.type();
        this.widgetRefs = widgetRefs;
    }

	public Integer getColNumber() {
		return colNumber;
	}

	public String getHtml() {
		return html;
	}

	public Map<String, Serializable> toMap() {
		Map<String,Serializable> map = new HashMap<String, Serializable>();
		map.put("colnumber", colNumber);
		map.put("html", html);
        map.put("type", type);
        map.put("widgetRefs", (Serializable) widgetRefs);
		return map;
	}

	public HtmlContent insertBefore(int i, String html) {
		return parent.insertContentBefore(this,i,html);

	}

	public void remove() {
		parent.removeContent(this);

	}

	public void setHtml(String html, CoreSession session) throws ClientException {
		this.html = html;
		parent.update(session);

	}

	public void setColNumber(int colNumber, CoreSession session) throws ClientException {
		this.colNumber = colNumber;
		parent.update(session);
	}

    public String getType() {
        return type;
    }

    public void setType(String type, CoreSession session) throws ClientException {
        this.type = type;
        parent.update(session);
    }

    public List<LabsWidget> getGadgets(CoreSession session) throws ClientException {
        ArrayList<LabsWidget> list = new ArrayList<LabsWidget>();
        if (Type.WIDGET_CONTAINER.type().equals(getType())) {
            if (widgetRefs.size() != widgets.size()) {
                for (String ref : widgetRefs) {
                    if (!widgets.containsKey(ref)) {
                        if (!ref.startsWith(WidgetType.HTML.type())) {
                            try {
                                if (session.exists(new IdRef(ref))) {
                                    widgets.put(ref, new LabsOpensocialGadget(session.getDocument(new IdRef(ref))));
                                } else {
                                    widgets.put(ref,new LabsHtmlWidget(ref));
                                }
                            } catch (ClientException e) {
                                LOG.error(e, e);
                            }
                        } else {
                            widgets.put(ref,new LabsHtmlWidget(ref.split("/")[1]));
                        }
                    }
                }
                Collection<String> toRemove = new LinkedList<String>();
                for (String ref : widgets.keySet()) {
                    if (!widgetRefs.contains(ref)) {
                        toRemove.add(ref);
                    }
                }
                if (widgetRefs.removeAll(toRemove)) {
                    parent.update(session);
                }
            }
            for (String ref : widgetRefs) {
                list.add(widgets.get(ref));
            }
            return list;
        }
        return list;
    }

    public void addWidgetRef(String ref, CoreSession session) throws ClientException {
        widgetRefs.add(ref);
        parent.update(session);
    }

    public void removeWidgetRef(String ref, CoreSession session) throws ClientException {
        widgetRefs.remove(ref);
        widgets.remove(ref);
        parent.update(session);
    }
    /**
     * @param session
     * @return <code>true</code> if gadgets were removed and session saved, otherwise <code>false</code>.
     */
    public boolean removeGadgets(CoreSession session) {
        try {
            LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
            return service.removeAllGadgetsOfHtmlContent(this, session);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }
}
