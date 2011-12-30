/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.ThemePropertiesException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;

/**
 * @author fvandaele
 *
 */
public class ThemePropertiesManage {
    
    public static final String DEFAULT_SEPARATOR_PROPERTIES = ";";
    private Map<String, ThemeProperty> properties;

    public ThemePropertiesManage() {
        properties = new HashMap<String, ThemeProperty>();
    }

    public ThemePropertiesManage(Map<String, ThemeProperty> properties) {
        this.properties = properties;
    }
    
    /**
     * If no file, return true
     * @param pathFile
     * @param time
     * @return
     */
    public boolean isLoaded(String pathFile, long time){
        if (!StringUtils.isEmpty(pathFile)){
            File fileProperties = new File(pathFile);
            if (fileProperties.exists()){
                return time >= fileProperties.lastModified();
            }
        }
        return true;
    }
    
    public void loadProperties(InputStream input) throws ThemePropertiesException{
        List<ThemeProperty> properties = new ArrayList<ThemeProperty>();
        if (input != null){
            try {
                InputStreamReader fr = new InputStreamReader(input) ;
                BufferedReader br = new BufferedReader(fr);
                String line;
                String separator = extractSeparator(br);
                line = br.readLine();
                while(line != null){
                    if (!StringUtils.isEmpty(line)){
                        if(line.startsWith("@")){
                            properties.add(extractProperty(line.trim(), separator)); 
                        }
                    }
                    line = br.readLine();
                }
                fr.close();
            } catch (Exception e) {
                throw new ThemePropertiesException("Problem with the propertie's file loading", e);
            }
        }
        addMapKeysOnAMapIfUnexist(properties);
    }

    /**
     * @param br
     * @return
     * @throws IOException
     */
    private String extractSeparator(BufferedReader br) throws IOException {
        String separator = null;
        String line = br.readLine();
        if (!StringUtils.isEmpty(line)){
            if(!line.startsWith("@")){
                separator = line.trim();
            }
        }
        if(StringUtils.isEmpty(separator)){
            separator = DEFAULT_SEPARATOR_PROPERTIES;
        }
        return separator;
    }
    
    private ThemeProperty extractProperty(String line, String separator) throws ThemePropertiesException{
        ThemeProperty prop = new ThemeProperty();
        String[] fields = line.split(separator);
        if(fields.length > 0){
            prop.setKey(fields[0]);
        }
        else{
            throw new ThemePropertiesException("The properties file is incorrectly formatted !"); 
        }
        if(fields.length > 1){
            prop.setLabel(fields[1]);
        }
        if(fields.length > 2){
            prop.setDescription(fields[2]);
        }
        return prop;
    }
    
    private void addMapKeysOnAMapIfUnexist(List<ThemeProperty> addedProperties){
        if(!addedProperties.isEmpty()){
            for (ThemeProperty addedProperty : addedProperties){
                if (!properties.containsKey(addedProperty)){
                    properties.put(addedProperty.getKey(), addedProperty);
                }
            }
        }
    }

    public Map<String, ThemeProperty> getProperties() {
        return properties;
    }
    
}
