/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean;

/**
 * @author fvandaele
 * 
 * Format of date
 *
 */
public enum LabsFormatDate {

    EEEE_DD_MMMMM_YYYY("format.date.eeee.dd.mmmmm.yyyy"),
    DD_MMMMM_YYYY("format.date.dd.mmmmm.yyyy"),
    DD_MM_YYYY("format.date.dd.mm.yyyy"),
    DD_MM_YY("format.date.dd.mm.yy"),
    YY_MM_DD("format.date.yy.mm.dd");
    
    private String i18n;
    
    LabsFormatDate(String i18n){
        this.i18n = i18n;
    }
    
    public String getI18n(){
        return i18n;
    }
}
