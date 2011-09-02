/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

/**
 * @author fvandaele
 * 
 * Type of data in the page list
 *
 */
public enum PageListEntryType {
    
    TEXT("text"),
    DATE("date"),
    CHECKBOX("checkbox"),
    SELECT("select"),
    URL("url");
    
    private String i18n;
    
    PageListEntryType(String i18n){
        this.i18n = i18n;
    }
    
    public String getI18n(){
        return i18n;
    }
}
