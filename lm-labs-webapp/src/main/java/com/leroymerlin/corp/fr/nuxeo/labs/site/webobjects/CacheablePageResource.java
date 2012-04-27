package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.freemarker.CacheBlock;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;

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
        String principalName = ctx.getPrincipal().getName();
        boolean isVisitor = true;
        try {
            LabsSite site = CommonHelper.siteDoc(doc).getSite();
            if (site.isContributor(principalName) || site.isAdministrator(principalName)) {
                isVisitor = false;
            }
        } catch (ClientException e) {
            LOG.warn("Unable to get site adapter while generating cache key so cached page is used: " + e.getMessage());
        }
        if (isVisitor) {
            return generateCacheName() + "-" + getPath();
        } else {
            return CacheBlock.NO_CACHE_KEY;
        }
    }
}
