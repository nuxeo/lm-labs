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
    
    TEXT("label.pageList.edit.editHeader.options.text"),
    DATE("label.pageList.edit.editHeader.options.date"),
    CHECKBOX("label.pageList.edit.editHeader.options.checkbox"),
    //CHECKBOXSEVERAL("label.pageList.edit.editHeader.options.checkboxSeveral"),
    SELECT("label.pageList.edit.editHeader.options.select"),
    URL("label.pageList.edit.editHeader.options.url");
    
    private String i18n;
    
    PageListEntryType(String i18n){
        this.i18n = i18n;
    }
    
    public String getI18n(){
        return i18n;
    }
}
