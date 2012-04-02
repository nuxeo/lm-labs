package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

public class FontSize {

    protected String size;
    
    protected String displayName;

    public FontSize(String size, String displayName) {
        this.size = size;
        this.displayName = displayName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
