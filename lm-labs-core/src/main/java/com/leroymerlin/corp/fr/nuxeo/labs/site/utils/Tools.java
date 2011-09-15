/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.model.impl.ListProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.LongProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.StringProperty;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;

/**
 * @author fvandaele
 *
 */
public class Tools {
    
    /**
     * Get a integer on the LongProperty
     * If the value is null, then get the WITHOUT_INT_VALUE
     * @param obj
     * @return a integer
     * @throws PropertyException
     * @throws NullException 
     */
    public static int getInt(Object obj) throws NullException, PropertyException{
        if (obj != null){
            Long value = ((LongProperty) obj).getValue(Long.class);
            if (value != null){
                return value.intValue();
            }
        }
        throw new NullException("This object is null.");
    }
    
    /**
     * Get a string on the StringProperty
     * @param obj
     * @return a string
     * @throws PropertyException
     */
    public static String getString(Object obj) throws PropertyException{
        if (obj != null){
            String value = ((StringProperty) obj).getValue(String.class);
            return value;
        }
        return null;
    }
    
    /**
     * Get a string on the StringProperty
     * @param obj
     * @return a List Of String
     * @throws PropertyException
     */
    public static List<String> getStringList(Object obj) throws PropertyException{
        if (obj != null){
            @SuppressWarnings("unchecked")
            List<String> value = ((ListProperty) obj).getValue(ArrayList.class);
            return value;
        }
        return null;
    }

}
