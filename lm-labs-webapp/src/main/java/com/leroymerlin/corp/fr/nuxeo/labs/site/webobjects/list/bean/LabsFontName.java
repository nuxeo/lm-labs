/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean;

/**
 * @author fvandaele
 * 
 * Type of data in the page list
 *
 */
public enum LabsFontName {

    ARIAL("label.font.name.arial"),
    VERDANA("label.font.name.verdana");
    
    private String i18n;
    
    LabsFontName(String i18n){
        this.i18n = i18n;
    }
    
    public String getI18n(){
        return i18n;
    }
}
