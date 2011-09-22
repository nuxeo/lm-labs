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
public enum ColSize {

    S_8PX(8),
    S_10PX(10);
    
    private int size;
    
    ColSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return size;
    }
}
