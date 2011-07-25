package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.apache.commons.lang.StringUtils;

public final class LabsSiteConstants {
    
    public enum Docs {
        SITESROOT("SitesRoot", "sites"),
        SITE("Site", StringUtils.EMPTY),
        TREE("Tree", "tree"),
        ASSETS("Assets", "assets"),
        PAGE("Page", StringUtils.EMPTY),
        PAGEBLOCS("PageBlocs", StringUtils.EMPTY),
        PAGENEWS("PageNews", StringUtils.EMPTY),
        PAGECLASSEUR("PageClasseur", StringUtils.EMPTY),
        PAGELISTE("PageListe", StringUtils.EMPTY),
        FOLDER("Folder", StringUtils.EMPTY),
        WELCOME(PAGEBLOCS.type(), "welcome");
        
        private String name;
        private String docType;

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
    }

    private LabsSiteConstants() {}

}
