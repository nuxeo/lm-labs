package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public final class LabsSiteConstants {
    
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
        WELCOME(PAGEBLOCS.type(), "welcome"),
        HTMLPAGE("HtmlPage",StringUtils.EMPTY);
        
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
    
    private LabsSiteConstants() {}

}
