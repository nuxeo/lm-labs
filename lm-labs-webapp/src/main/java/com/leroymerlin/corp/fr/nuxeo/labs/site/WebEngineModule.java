package com.leroymerlin.corp.fr.nuxeo.labs.site;

import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.PageBlocs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.PageClasseurResource;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.Site;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.SitesRoot;

public class WebEngineModule extends org.nuxeo.ecm.webengine.app.WebEngineModule {

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.webengine.app.WebEngineModule#getWebTypes()
     */
    @Override
    public Class<?>[] getWebTypes() {
        return new Class<?>[] { LabsNews.class, PageBlocs.class, PageClasseurResource.class, Site.class, SitesRoot.class };
    }

}
