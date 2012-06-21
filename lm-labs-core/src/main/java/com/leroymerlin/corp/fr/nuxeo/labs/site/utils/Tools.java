/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.model.impl.ListProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.BooleanProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.LongProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.StringProperty;

import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsSession;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;

/**
 * @author fvandaele
 *
 */
public class Tools {
	
    private static final Log LOG = LogFactory.getLog(Tools.class);
    
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
    

    public static boolean getBoolean(Object obj) throws NullException, PropertyException{
    	if (obj != null){
            Boolean value = ((BooleanProperty) obj).getValue(Boolean.class);
            if (value != null){
                return value.booleanValue();
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
    
    /**
     * Return the adapter and set the session if itf implements LabsSession
     * @param itf
     * @param document
     * @param session
     * @return
     */
    public static <T> T getAdapter(Class<T> itf, DocumentModel document, CoreSession session){
    	T adapter = null;
    	if (document != null){
    		adapter = document.getAdapter(itf);
			if (adapter != null && hasInterfaceLabsSession(itf)){
				if (session == null){
					LOG.error("No session for adapter " + itf.getName() + "; This is mandatory! - IN : " + adapter.getClass().getName());
					NullException nunu = new NullException("adapter" + adapter.getClass().getName());
					nunu.printStackTrace();
				}
				((LabsSession)adapter).setSession(session);
			}
    	}
    	return adapter;
    }
    
    private static <T> boolean hasInterfaceLabsSession(Class<T> itf){
    	return hasInterface(itf, LabsSession.class);
    }
	
	/**
	 * return true if itf has interface itf2
	 * @param itf
	 * @param itf2
	 * @return
	 */
	public static <T, Y> boolean hasInterface(Class<T> itf, Class<Y> itf2){
		if(itf2.getName().equals(itf.getName())){
			return true;
		}
		else{
			Class<?>[] interfaces = itf.getInterfaces();
			if (interfaces.length > 0){
				for (Class<?> myInterface : interfaces){
					if (hasInterface(myInterface, itf2)){
						return true;
					}
				}
			}
		}
		return false;
	}

}
