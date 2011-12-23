/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;

/**
 * @author fvandaele
 *
 */
public class Header implements Comparable<Header> {
    
    public static final String DEFAULT = "default";

    private static final String IS_MALFORMED = "' is malformed.";

    private static final String THE_FONT_CODE = "The font code '";

    private static final String THE_FONT_CODE_DON_T_BE_NULL = "The font code don't be null.";

    private String name;
    
    private String type;
    
    private String width;
    
    private String fontName;
    
    private String font;
    
    private String fontSize;

    private int idHeader = -1;
    
    private int orderPosition = -1;

    private List<String> selectlist;

    private String formatDate;

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
    
    @Override
    public int compareTo(Header obj) {
        return new Integer(getOrderPosition()).compareTo(new Integer(obj.getOrderPosition()));
    }
    
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
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
    
    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public List<String> getSelectlist() {
        if (selectlist == null){ 
            selectlist = new ArrayList<String>();
        }
        return selectlist;
    }

    public void setSelectlist(List<String> select) {
        this.selectlist = select;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        if (StringUtil.isEmpty(font)){
            throw new IllegalArgumentException(THE_FONT_CODE_DON_T_BE_NULL); 
        }
        this.font = font;
        extractElementsOfFont();
    }

    public void extractElementsOfFont() {
        if (!font.contains(DEFAULT)){
            String[] split = font.split("-");
            if (split.length == 2){
                this.fontName = split[0];
                this.fontSize = split[1];
            }
            else{
                throw new IllegalArgumentException(THE_FONT_CODE + font + IS_MALFORMED);
            }
        }
        else{
            this.fontName = null;
            this.fontSize = null;
        }
    }
    
    public void createFont(){
        if (StringUtils.isEmpty(fontName) || StringUtils.isEmpty(fontSize)){
            font = DEFAULT;
        }
        else{
            font = fontName + '-' + fontSize;
        }
    }

    public void setOrderPosition(int orderPosition) {
        this.orderPosition = orderPosition;
    }

    public int getOrderPosition() {
        return orderPosition;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }
}
