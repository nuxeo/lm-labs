package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public final class CommonHelper {

    public CommonHelper() {}

    public static final SiteDocument siteDoc(DocumentModel doc) {
        return doc.getAdapter(SiteDocument.class);
    }

    public static final Page sitePage(DocumentModel doc) throws ClientException {
        Page page = null;
        if (LabsSiteConstants.Docs.SITE.type().equals(doc.getType())){
            DocumentModel homePage = doc.getAdapter(LabsSite.class).getIndexDocument();
            page = homePage.getAdapter(Page.class);
        }
        else{
            page = doc.getAdapter(Page.class);
        }
        return page;
    }
    
    public static final List<String> getNotifiableTypes() {
        List<String> list = new ArrayList<String>();
        for (Docs doc : Docs.notifiableDocs()) {
            list.add(doc.type());
        }
        return list;
    }
    
    public static final List<Page> getTopNavigationPages(DocumentModel siteDoc, String userName) throws ClientException {
        List<Page> pages = new ArrayList<Page>();
        LabsSite site = siteDoc(siteDoc).getSite();
        Collection<Page> allTopPages = siteDoc(site.getTree()).getChildrenPages();
        final DocumentModel homePageDoc = site.getIndexDocument();
        Page homePage = homePageDoc.getAdapter(Page.class);
        if (!site.isAdministrator(userName)) {
            if (homePage.isVisible() && !homePage.isDeleted()) {
                pages.add(homePage);
            }
            pages.addAll(CollectionUtils.select(allTopPages, new Predicate() {
                @Override
                public boolean evaluate(Object input) {
                    Page page = (Page) input;
                    if (page.getDocument().getId().equals(homePageDoc.getId())) {
                        return false;
                    }
                    try {
                        return (page.isVisible() && !page.isDeleted());
                    } catch (ClientException e) {
                        return false;
                    }
                }}));
        } else {
            pages.add(homePage);
            pages.addAll(CollectionUtils.select(allTopPages, new Predicate() {
                @Override
                public boolean evaluate(Object input) {
                    Page page = (Page) input;
                    if (page.getDocument().getId().equals(homePageDoc.getId())) {
                        return false;
                    }
                    return true;
                }}));
        }
        return pages;
        
    }
}
