package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

import edu.emory.mathcs.backport.java.util.Collections;

public final class LabsSiteConstants {
    
    public final class NotifNames {
        public static final String NEWS_PUBLISHED = "News published";
        public static final String PAGE_MODIFIED = "Page modified";
        public static final String PAGE_REMOVED = "Page removed";
    }
    
    public final class EventNames {
        public static final String NEWS_PUBLISHED_UNDER_PAGENEWS = "newsPublishedUnderPageNews";
        @Deprecated
        public static final String CHECK_PUBLISHED_NEWS_TO_NOTIFY = "checkPublishedNewsToNotify";
        public static final String PAGE_MODIFIED = "pageModified";
        public static final String CHECK_PAGES_TO_NOTIFY = "checkPagesToNotify";
        public static final String PAGE_REMOVED = "pageRemoved";
    }

    public final class FacetNames {
        public static final String LABSPAGE = "LabsPage";
        public static final String LABSTEMPLATE = "LabsTemplate";
        public static final String LABSTHEME = "LabsTheme";
    }
    
    public enum Schemas {
        PAGE("page", "pg"),
        PAGENEWS("page_news", "pgn"),
        LABSNEWS("labsnews", "ln"),
        PAGELIST("page_list", "pgl"),
        PAGELIST_LINE("page_list_line", "pgll"),
        PAGEBLOCS("page_blocs", "pgb"),
        PAGEHTML("page_html", "html"),
        EXTERNALURL("external_url", "exturl"),
        LABSTEMPLATE("labstemplate", "labstemplate"),
        LABSSITE("labssite", "labssite"),
        SITETHEME("sitetheme", "sitetheme");

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
        EXTERNAL_URLS("OrderedFolder", "externalUrls"),
        PAGENEWS("PageNews", StringUtils.EMPTY),
        LABSNEWS("LabsNews", StringUtils.EMPTY),
        PAGECLASSEUR("PageClasseur", StringUtils.EMPTY),
        PAGECLASSEURFOLDER("Folder", StringUtils.EMPTY),
        PAGELIST("PageList", StringUtils.EMPTY),
        PAGELIST_LINE("PageListLine", StringUtils.EMPTY),
        FOLDER("Folder", StringUtils.EMPTY),
        HTMLPAGE("HtmlPage",StringUtils.EMPTY),
        DASHBOARD("Space",StringUtils.EMPTY),
        NOTIFACTIVITIES("NotificationActivities", "notifActivities"),
        SITETHEME("SiteTheme",StringUtils.EMPTY),
        WELCOME(HTMLPAGE.type(), "Accueil"); // TODO

        private String docType;
        private String name;

        private static final Map<String, Docs> stringToEnum = new HashMap<String, Docs>();
        static { // Initialize map from constant name to enum constant
            for (Docs op : values())
                if (op != WELCOME) {
                    stringToEnum.put(op.type(), op);
                }
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
            return EnumSet.of(HTMLPAGE, PAGE, DASHBOARD, PAGEBLOCS, PAGECLASSEUR, PAGELIST, PAGENEWS, WELCOME);
        }

        public static EnumSet<Docs> notifiableDocs() {
            return EnumSet.of(HTMLPAGE, DASHBOARD, PAGECLASSEUR, PAGELIST, PAGENEWS);
        }

        public static EnumSet<Docs> labsLifeCycleDocs() {
            return EnumSet.of(SITE, PAGE, PAGEBLOCS, PAGENEWS, PAGECLASSEUR, PAGELIST, HTMLPAGE, DASHBOARD);
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
    
    /*
     * Define in the file 'lifecycle-contrib.xml'
     */
    public static String LIFE_CYCLE_LABS = "labs";
    
    public static List<String> OREDERED_PERMISSONS;
    
    public static PermissionsHelper  PERMISSIONS_HELPER;

    static{
        OREDERED_PERMISSONS = new ArrayList<String>();
        OREDERED_PERMISSONS.add(SecurityConstants.EVERYTHING);
        OREDERED_PERMISSONS.add(SecurityConstants.READ_WRITE);
        OREDERED_PERMISSONS.add(SecurityConstants.READ);
        PERMISSIONS_HELPER = new PermissionsHelper(OREDERED_PERMISSONS);
    }

    public enum Rights {

        EVERYTHING(SecurityConstants.EVERYTHING), WRITE(
                SecurityConstants.READ_WRITE), READ(SecurityConstants.READ);

        private String right;

        Rights(String right) {
            this.right = right;
        }

        public String getRight() {
            return right;
        }
    }
    
    public enum PropertyType {

        COLOR("color"), FONT("font"), SIZE("size"), STRING("string"), IMAGE("image");

        private String type;
        
        private static final Map<String, PropertyType> stringToEnum = new HashMap<String, PropertyType>();
        static { // Initialize map from constant name to enum constant
            for (PropertyType op : values())
                stringToEnum.put(op.getType(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static PropertyType fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        PropertyType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
    
    public enum FontFamily {

        ARIAL("Arial", "arial, sans-serif"), TIMES_NEW_ROMAN("Times new roman", "'times new roman', serif"), 
            COURIER_NEW("Courier New", "'courier new', monospace"), GEORGIA("Georgia", "georgia, serif"), 
            TREBUCHET("Trebuchet", "'trebuchet ms', sans-serif"), VERDANA("Verdana", "verdana, sans-serif");

        private String displayName;
        private String cssName;
        
        private static final Map<String, FontFamily> stringToEnum = new HashMap<String, FontFamily>();
        static { // Initialize map from constant name to enum constant
            for (FontFamily op : values())
                stringToEnum.put(op.getDisplayName(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static FontFamily fromString(String symbol) {
            return stringToEnum.get(symbol);
        }
        
        public static List<FontFamily> getFontFamilies(){
            ArrayList<LabsSiteConstants.FontFamily> list = new ArrayList<LabsSiteConstants.FontFamily>(stringToEnum.values());
            Collections.sort(list);
            return list;
        }

        FontFamily(String displayName, String cssName) {
            this.displayName = displayName;
            this.cssName = cssName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getCssName() {
            return cssName;
        }
    }
    
    public enum FontSize {

        S_8PX("8px"), S_10PX("10px"), S_12PX("12px"), S_16PX("16px"),S_18PX("18px"), S_24PX("24px");

        private String size;
        
        private static final Map<String, FontSize> stringToEnum = new HashMap<String, FontSize>();
        static { // Initialize map from constant name to enum constant
            for (FontSize op : values())
                stringToEnum.put(op.getSize(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static FontSize fromString(String symbol) {
            return stringToEnum.get(symbol);
        }
        
        public static List<FontSize> getFontSizes(){
            ArrayList<LabsSiteConstants.FontSize> list = new ArrayList<LabsSiteConstants.FontSize>(stringToEnum.values());
            Collections.sort(list);
            return list;
        }

        FontSize(String type) {
            this.size = type;
        }

        public String getSize() {
            return size;
        }
        
    }

    private LabsSiteConstants() {}

}
