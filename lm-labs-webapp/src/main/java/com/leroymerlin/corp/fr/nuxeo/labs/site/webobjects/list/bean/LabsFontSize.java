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
public enum LabsFontSize {

    S_8PX(8),
    S_10PX(10),
    S_12PX(12),
    S_14PX(14),
    S_16PX(16),
    S_18PX(18),
    S_20PX(20),
    S_24PX(24);
    
    private int size;
    
    LabsFontSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return size;
    }
}
