package com.leroymerlin.corp.fr.nuxeo.labs.site.util;

import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.CreateSitePopup;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;

public final class SiteHelper {

    private SiteHelper() {}
    
    public static void createSite(SitesRootPage page, String title, String url, String descr) {
        
        CreateSitePopup create = fillCreateSitePopup(page, title, url, descr);
        create.validate();
    }

    private static CreateSitePopup fillCreateSitePopup(SitesRootPage homePage, String title, String url, String descr) {
        CreateSitePopup createSite = homePage.createSite();
        createSite.setTitle(title);
        createSite.setURL(url);
        createSite.setDescription(descr);
        return createSite;
    }

}
