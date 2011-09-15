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
public enum LabsFontSize {

    S_8PX(8),
    S_10PX(10);
    
    private int size;
    
    LabsFontSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return size;
    }
}
