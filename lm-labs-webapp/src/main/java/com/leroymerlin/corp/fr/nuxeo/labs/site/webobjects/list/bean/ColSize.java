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
    S_10PX(10),
    S_15PX(15),
    S_20PX(20),
    S_30PX(30),
    S_40PX(40),
    S_50PX(50),
    S_70PX(70),
    S_100PX(100);
    
    private int size;
    
    ColSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return size;
    }
}
