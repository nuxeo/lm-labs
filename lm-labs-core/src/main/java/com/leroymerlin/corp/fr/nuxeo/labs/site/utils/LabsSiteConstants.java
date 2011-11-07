package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

public final class LabsSiteConstants {
    
    public final class NotifNames {
        public static final String NEWS_PUBLISHED = "News published";
    }
    
    public final class EventNames {
        public static final String NEWS_PUBLISHED_UNDER_PAGENEWS = "newsPublishedUnderPageNews";
        public static final String CHECK_PUBLISHED_NEWS_TO_NOTIFY = "checkPublishedNewsToNotify";
    }

    public final class FacetNames {
        public static final String LABSPAGE = "LabsPage";
    }
    
    public enum Schemas {
        PAGE("page", "pg"),
        PAGENEWS("page_news", "pgn"),
        NEWS("labsnews", "ln"),
        PAGELIST("page_list", "pgl"),
        PAGELIST_LINE("page_list_line", "pgll"),
        PAGEBLOCS("page_blocs", "pgb"),
        EXTERNALURL("external_url", "exturl"),
        HTMLPAGE("page_html", "html");

        private String name;
        private String prefix;

        private Schemas(String name, String prefix) {
            this.name = name;
            this.prefix = prefix;
        }

        /**
         * @return schema name
         */
        public String getName() {
            return name;
        }

        public String prefix() {
            return prefix;
        }
    }

    public enum Docs {
        DEFAULT_DOMAIN("Domain", "default-domain"),
        SITESROOT("SitesRoot", "sites"),
        SITE("Site", StringUtils.EMPTY),
        TREE("Tree", "tree"),
        ASSETS("Assets", "assets"),
        PAGE("Page", StringUtils.EMPTY),
        PAGEBLOCS("PageBlocs", StringUtils.EMPTY),
        EXTERNAL_URL("ExternalURL", StringUtils.EMPTY),
        PAGENEWS("PageNews", StringUtils.EMPTY),
        LABSNEWS("LabsNews", StringUtils.EMPTY),
        PAGECLASSEUR("PageClasseur", StringUtils.EMPTY),
        PAGELIST("PageList", StringUtils.EMPTY),
        PAGELIST_LINE("PageListLine", StringUtils.EMPTY),
        FOLDER("Folder", StringUtils.EMPTY),
        HTMLPAGE("HtmlPage",StringUtils.EMPTY),
        DASHBOARD("Space",StringUtils.EMPTY),
        WELCOME(DASHBOARD.type(), "welcome"); // TODO

        private String docType;
        private String name;

        private static final Map<String, Docs> stringToEnum = new HashMap<String, Docs>();
        static { // Initialize map from constant name to enum constant
            for (Docs op : values())
                stringToEnum.put(op.type(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static Docs fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        private Docs(String docType, String name) {
            this.name = name;
            this.docType = docType;
        }

        /**
         * @return document name of document type which can be instantiated only once otherwise an empty string.
         */
        public String docName() {
            return name;
        }

        /**
         * @return document type.
         */
        public String type() {
            return docType;
        }

        public static EnumSet<Docs> pageDocs() {
            return EnumSet.of(HTMLPAGE, PAGE, PAGEBLOCS, PAGECLASSEUR, PAGELIST, PAGENEWS ,WELCOME);
        }
    }
    
    public enum State {
        DRAFT("draft", "draft"),
        PUBLISH("publish", "published"),
        DELETE("delete", "deleted"),
        UNDELETE("undelete", null);

        private String transition;
        private String state;

        private static final Map<String, State> stringToEnum = new HashMap<String, State>();
        static { // Initialize map from constant name to enum constant
            for (State op : values())
                stringToEnum.put(op.getState(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static State fromString(String symbol) {
            return stringToEnum.get(symbol);
        }
        private State(String transition, String state) {
            this.transition = transition;
            this.state = state;
        }

        /**
         * @return transition name
         */
        public String getTransition() {
            return transition;
        }

        /**
         * @return state name
         */
        public String getState() {
            return state;
        }

    }
    
    public static List<String> OREDERED_PERMISSONS;
    
    public static PermissionsHelper  PERMISSIONS_HELPER;

    static{
        OREDERED_PERMISSONS = new ArrayList<String>();
        OREDERED_PERMISSONS.add(SecurityConstants.EVERYTHING);
        OREDERED_PERMISSONS.add(SecurityConstants.READ_WRITE);
        OREDERED_PERMISSONS.add(SecurityConstants.READ);
        PERMISSIONS_HELPER = new PermissionsHelper(OREDERED_PERMISSONS);
    }

    private LabsSiteConstants() {}

}
