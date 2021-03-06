/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean;

import java.util.EnumSet;

/**
 * @author fvandaele
 *
 * Type of data in the page list
 *
 */
public enum EntryType {

    TEXT("label.pageList.edit.editHeader.options.text", ""),
    DATE("label.pageList.edit.editHeader.options.date", ""),
    CHECKBOX("label.pageList.edit.editHeader.options.checkbox", ""),
    //CHECKBOXSEVERAL("label.pageList.edit.editHeader.options.checkboxSeveral", ""),
    SELECT("label.pageList.edit.editHeader.options.select", ""),
    CREATOR("label.pageList.edit.editHeader.options.creator", "dc:creator"),
    MODIFIED("label.pageList.edit.editHeader.options.modified", "dc:modified"),
    CREATED("label.pageList.edit.editHeader.options.created", "dc:created"),
    FILES("label.pageList.edit.editHeader.options.attachedFiles", ""),
    URL("label.pageList.edit.editHeader.options.url", ""),
    TEXTAREA("label.pageList.edit.editHeader.options.textarea", "");

    private String i18n;
    private String xpath;

    EntryType(String i18n, String xpath) {
        this.i18n = i18n;
        this.xpath = xpath;
    }

    public String getI18n() {
        return i18n;
    }

    public String xpath() {
        return this.xpath;
    }

    public static EnumSet<EntryType> linePropTypes() {
        return EnumSet.of(CREATOR, MODIFIED, CREATED);
    }
}
