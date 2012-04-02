package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

public class FontFamily {

    protected String cssName;

    protected String displayName;

    public FontFamily(String cssName, String displayName) {
        this.cssName = cssName;
        this.displayName = displayName;
    }

    public String getCssName() {
        return cssName;
    }

    public void setCssName(String cssName) {
        this.cssName = cssName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
        
}
