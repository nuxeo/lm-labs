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

    S_25PX(25),
    S_50PX(50),
    S_75PX(75),
    S_100PX(100),
    S_150PX(150),
    S_200PX(200),
    S_250PX(250),
    S_300PX(300),
    S_350PX(350),
    S_400PX(400),
    S_450PX(450),
    S_500PX(500),
    S_550PX(550),
    S_600PX(600);
    
    private int size;
    
    ColSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return size;
    }
}
