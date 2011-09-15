/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

/**
 * @author fvandaele
 * 
 */
public class LabsFontDto {

    private Enum<LabsFontName> fontName;

    private Enum<LabsFontSize> fontSize;

    public LabsFontDto(Enum<LabsFontName> pFontName, Enum<LabsFontSize> pFontSize) {
        super();
        this.fontName = pFontName;
        this.fontSize = pFontSize;
    }

    public String getNameI18n() {
        return Enum.valueOf(LabsFontName.class, fontName.name()).getI18n();
    }

    public Enum<LabsFontName> getFontName() {
        return fontName;
    }

    public Enum<LabsFontSize> getFontSize() {
        return fontSize;
    }
}
