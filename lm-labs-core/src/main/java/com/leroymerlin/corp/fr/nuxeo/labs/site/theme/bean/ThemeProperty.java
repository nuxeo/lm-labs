/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean;

/**
 * @author fvandaele
 *
 */
public class ThemeProperty {

    private String key;
    
    private String value;
    
    private String label;
    
    private String description;
    
    private String type;
    
    public ThemeProperty() {
        this.key = "";
        this.value = "";
        this.label = "";
        this.description = "";
    }

    public ThemeProperty(String key, String value, String label, String description, String type) {
        super();
        this.key = key;
        this.value = value;
        this.label = label;
        this.description = description;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        ThemeProperty other = (ThemeProperty) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
