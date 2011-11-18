package com.leroymerlin.corp.fr.nuxeo.labs.site.publisher;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.NXCore;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;
import org.nuxeo.ecm.core.lifecycle.LifeCycleService;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsPublisherAdapterFactory implements DocumentAdapterFactory{
    
    private static final Log LOG = LogFactory.getLog(LabsPublisherAdapterFactory.class);

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        LifeCycleService service = null;
        try {
            service = NXCore.getLifeCycleService();
            Collection<String> types = service.getTypesFor(LabsSiteConstants.LIFE_CYCLE_LABS);
            if(types != null && types.contains(doc.getType())) {
                return new LabsPublisherAdapter(doc);
            }
        } catch (Exception e) {
            LOG.error("Problem while the loading of life cycle doctype", e);
        }
        return null;
    }

}
