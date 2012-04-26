package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

public final class LabsSiteConstants {
    
    public final class Comments{
        public static final String COMMENT_AUTHOR = "comment:author";
        public static final String COMMENT_TEXT = "comment:text"; 
        public static final String COMMENT_CREATION_DATE = "comment:creationDate"; 
    }
    
    public final class Forum{
        public static final String FORUM_CREATOR = "dc:creator";
    }
    
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
        public static final String LABSHIDDEN = "LabsHidden";
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
        LABSTOPIC("LMForumTopic", StringUtils.EMPTY),
        PAGECLASSEUR("PageClasseur", StringUtils.EMPTY),
        PAGECLASSEURFOLDER("Folder", StringUtils.EMPTY),
        PAGELIST("PageList", StringUtils.EMPTY),
        PAGELIST_LINE("PageListLine", StringUtils.EMPTY),
        FOLDER("Folder", StringUtils.EMPTY),
        HTMLPAGE("HtmlPage",StringUtils.EMPTY),
        DASHBOARD("Space",StringUtils.EMPTY),
        NOTIFACTIVITIES("NotificationActivities", "notifActivities"),
        SITETHEMESROOT("Folder", "themes"),
        SITETHEME("SiteTheme",StringUtils.EMPTY),
        PAGEFORUM("PageForum", StringUtils.EMPTY),
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
            return EnumSet.of(HTMLPAGE, PAGE, DASHBOARD, PAGEBLOCS, PAGECLASSEUR, PAGELIST, PAGENEWS, PAGEFORUM, WELCOME);
        }

        public static EnumSet<Docs> notifiableDocs() {
            return EnumSet.of(HTMLPAGE, DASHBOARD, PAGECLASSEUR, PAGELIST, PAGENEWS, PAGEFORUM, SITE);
        }

        public static EnumSet<Docs> labsLifeCycleDocs() {
            return EnumSet.of(SITE, PAGE, PAGEBLOCS, PAGENEWS, PAGECLASSEUR, PAGELIST, HTMLPAGE, PAGEFORUM, DASHBOARD);
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
    
    public enum Directories {
        COLUMNS_LAYOUT("columns_layout", "columns_layout", "code", "", "order"),
        PAGE_TEMPLATES("labs_page_templates", "vocabulary", "id", "label", "ordering"),
        THEMES("labs_themes", "vocabulary", "id", "label", "ordering"),
        FONT_SIZES("labs_fontsizes", "vocabulary", "id", "label", "ordering"),
        FONT_FAMILIES("labs_fontfamilies", "vocabulary", "id", "label", "label");

        private String dirName;
        private String schema;
        private String idField;
        private String labelField;
        private String orderingField;

        private Directories(String name, String prefix, String idField, String labelField, String orderingField) {
            this.dirName = name;
            this.schema = prefix;
            this.idField = idField;
            this.labelField = labelField;
            this.orderingField = orderingField;
        }

        /**
         * @return directory name
         */
        public String dirName() {
            return dirName;
        }

        /**
         * @return schema name
         */
        public String schema() {
            return schema;
        }
        
        public String idField() {
            return idField;
        }
        
        public String labelField() {
            return labelField;
        }
        
        public String orderingField() {
            return orderingField;
        }
    }

    private LabsSiteConstants() {}

}
