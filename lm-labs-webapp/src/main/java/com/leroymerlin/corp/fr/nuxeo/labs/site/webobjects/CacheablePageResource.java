package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.freemarker.CacheBlock;

public class CacheablePageResource extends NotifiablePageResource {

    private static final Log LOG = LogFactory.getLog(CacheablePageResource.class);
    
    protected String cacheKey = CacheBlock.NO_CACHE_KEY;
    
    protected String cacheName = "";

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        cacheKey = generateCacheKey();
        cacheName = generateCacheName();
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public String getCacheName() {
        return cacheName;
    }

    protected String generateCacheName() {
        return "LabsSites-" + getDocument().getType();
    }
    
    protected String generateCacheKey() {
        boolean isVisitor = true;
        String role = "V";
        String principalName = ctx.getPrincipal().getName();
        CoreSession session = ctx.getCoreSession();
        try {
            if (getPage().isAdministrator(principalName, session)) {
                role = "A";
            } else if (getPage().isContributor(principalName, session)) {
                role = "C";
            }
        } catch (ClientException e) {
            LOG.warn("Unable to get site adapter while generating cache key so cached page is used: " + e.getMessage());
        }
        if (isVisitor) {
            String dateStr = "";
            try {
                dateStr = Long.toString(((Calendar)getDocument().getPropertyValue("dc:modified")).getTime().getTime());
            } catch (PropertyException e) {
                LOG.warn("Unable to get property 'dc:modified': " + e.getMessage());
            } catch (ClientException e) {
                LOG.warn("Unable to get property 'dc:modified': " + e.getMessage());
            }
            return generateCacheName() + "-" + getPath() + "-" + role + "-" + dateStr;
        } else {
            return CacheBlock.NO_CACHE_KEY;
        }
    }
}
