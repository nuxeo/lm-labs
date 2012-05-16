package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

public interface LabsGadgetManager {

    public enum WidgetType {

        HTML("html"), OPENSOCIAL("opensocial");

        private String type;

        private static final Map<String, WidgetType> stringToEnum = new HashMap<String, WidgetType>();
        static { // Initialize map from constant name to enum constant
            for (WidgetType op : values())
                stringToEnum.put(op.type(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static WidgetType fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        WidgetType(String type) {
            this.type = type;
        }

        public String type() {
            return type;
        }
    }

    /**
     * @param content
     * @param htmlPageDoc
     * @param widget
     * @param session
     * @return widget reference or <code>null</code> if widget type is unknown
     * @throws ClientException
     */
    String addWidgetToHtmlContent(HtmlContent content, DocumentModel htmlPageDoc, LabsWidget widget, CoreSession session) throws ClientException;

    /**
     * Removes all widgets and save {@link CoreSession} if needed.
     * @param content
     * @param session
     * @return <code>true</code> if gadgets removed otherwise <code>false</code>
     * @throws ClientException
     */
    boolean removeAllGadgetsOfHtmlContent(HtmlContent content, CoreSession session) throws ClientException;
}
