/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto;

/**
 * @author fvandaele
 *
 */
public class Header {
    
    private String name;
    
    private String type;
    
    private int width;
    
    private String fontName;
    
    private int fontSize;

    private int idHeader;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idHeader;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Header other = (Header) obj;
        if (idHeader != other.idHeader)
            return false;
        return true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

}
