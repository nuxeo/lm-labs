package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;

public class AbstractTestOperation {

    public AbstractTestOperation() {
        super();
    }

    protected SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

}