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
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.PropertyType;

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
     *
     * @param pathFile
     * @param time
     * @return
     */
    public boolean isLoaded(String pathFile, long time) {
        if (!StringUtils.isEmpty(pathFile)) {
            File fileProperties = new File(pathFile);
            if (fileProperties.exists()) {
                return time >= fileProperties.lastModified();
            }
        }
        return true;
    }

    public void loadProperties(InputStream input)
            throws ThemePropertiesException {
        List<ThemeProperty> propertiesFile = new ArrayList<ThemeProperty>();
        InputStreamReader inputReader = null;
        if (input != null) {
            try {
                inputReader = new InputStreamReader(input);
                BufferedReader buffer = new BufferedReader(inputReader);
                String line;
                String separator = extractSeparator(buffer);
                line = buffer.readLine();
                int i = 1;
                while (line != null) {
                    if (!StringUtils.isEmpty(line)) {
                        if (line.startsWith("@")) {
                            propertiesFile.add(extractProperty(line.trim(),
                                    separator, i++));
                        }
                    }
                    line = buffer.readLine();
                }
            } catch (Exception e) {
                throw new ThemePropertiesException(
                        "Problem with the propertie's file loading", e);
            } finally {
                closeInputStream(inputReader);
            }
        }
        merge(propertiesFile);
    }

    /**
     * @param input
     * @throws ThemePropertiesException
     */
    private void closeInputStream(InputStreamReader input)
            throws ThemePropertiesException {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                throw new ThemePropertiesException(
                        "Problem with the propertie's file closing", e);
            }
        }
    }

    /**
     * @param br
     * @return
     * @throws IOException
     */
    private String extractSeparator(BufferedReader br) throws IOException {
        String separator = null;
        String line = br.readLine();
        if (!StringUtils.isEmpty(line)) {
            if (!line.startsWith("@")) {
                separator = line.trim();
            }
        }
        if (StringUtils.isEmpty(separator)) {
            separator = DEFAULT_SEPARATOR_PROPERTIES;
        }
        return separator;
    }

    private ThemeProperty extractProperty(String line, String separator,
            int orderNumber) throws ThemePropertiesException {
        ThemeProperty prop = new ThemeProperty();
        String[] fields = line.split(separator);
        if (fields.length > 0) {
            prop.setKey(fields[0]);
        } else {
            throw new ThemePropertiesException(
                    "The properties file is incorrectly formatted !");
        }
        if (fields.length > 1) {
            prop.setLabel(fields[1]);
        }
        if (fields.length > 2) {
            prop.setDescription(fields[2]);
        }
        if (fields.length > 3) {
            prop.setType(PropertyType.fromString(fields[3]));
        } else {
            prop.setType(PropertyType.STRING);
        }
        prop.setOrderNumber(orderNumber);
        return prop;
    }

    private void merge(List<ThemeProperty> addedProperties) {
        Map<String, ThemeProperty> result = new HashMap<String, ThemeProperty>();

        if (!addedProperties.isEmpty()) {
            for (ThemeProperty addedProperty : addedProperties) {
                if (!properties.containsKey(addedProperty.getKey())) {
                    result.put(addedProperty.getKey(), addedProperty);
                } else {
                    ThemeProperty prop = properties.get(addedProperty.getKey());
                    prop.setLabel(addedProperty.getLabel());
                    prop.setDescription(addedProperty.getDescription());
                    prop.setType(addedProperty.getType());
                    prop.setOrderNumber(addedProperty.getOrderNumber());
                    result.put(prop.getKey(), prop);
                }
            }
            properties = result;
        }
    }

    public Map<String, ThemeProperty> getProperties() {
        return properties;
    }
}
