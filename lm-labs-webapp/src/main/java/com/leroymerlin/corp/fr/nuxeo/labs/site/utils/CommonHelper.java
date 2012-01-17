package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FontFamily;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FontSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

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
    
    public static final List<String> getLabsLifeCycleTypes() {
        List<String> list = new ArrayList<String>();
        for (Docs doc : Docs.labsLifeCycleDocs()) {
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
    
    public static String quoteURIPathComponent(String s) {
        return URIUtils.quoteURIPathComponent(s, false);
    }
    
    public static final List<LabsSite> getTemplateSites(CoreSession session) throws ClientException {
    	SiteManager sm;
    	List<LabsSite> adaptersList = new ArrayList<LabsSite>();
    	try {
			sm = Framework.getService(SiteManager.class);
		} catch (Exception e) {
			return adaptersList;
		}
    	StringBuilder query = new StringBuilder();
    	query.append("SELECT * FROM ").append(Docs.SITE.type())
    		.append(" WHERE ").append(NXQL.ECM_LIFECYCLESTATE).append(" = '").append(State.PUBLISH.getState()).append("'")
    		.append(" AND ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(sm.getSiteRoot(session).getPathAsString()).append("'")
    		.append(" AND ").append(LabsSiteAdapter.PROPERTY_SITE_TEMPLATE).append(" = 1");
    	DocumentModelList list = session.query(query.toString());
    	for(DocumentModel site : list) {
    		adaptersList.add(site.getAdapter(LabsSite.class));
    	}
    	return adaptersList;
    }
    
    public static final boolean canCreateSite(String principalId, CoreSession session) {
    	SiteManager sm = getSiteManager();
    	if (sm != null) {
    		try {
				DocumentModel siteRoot = sm.getSiteRoot(session);
				if(session.hasPermission(siteRoot.getRef(), SecurityConstants.ADD_CHILDREN)) {
					return true;
				}
			} catch (ClientException e) {
				return false;
			}
    	}
    	return false;
    }
    
    public static final boolean canCreateTemplateSite(String principalId, CoreSession session) {
    	SiteManager sm = getSiteManager();
    	if (sm != null) {
    		try {
				DocumentModel siteRoot = sm.getSiteRoot(session);
				if(session.hasPermission(siteRoot.getRef(), SecurityConstants.EVERYTHING)) {
					return true;
				}
			} catch (ClientException e) {
				return false;
			}
    	}
    	return false;
    }
    
    public static final boolean canDeleteSite(String principalId, CoreSession session) {
    	SiteManager sm = getSiteManager();
    	if (sm != null) {
    		try {
				DocumentModel siteRoot = sm.getSiteRoot(session);
				if(session.hasPermission(siteRoot.getRef(), SecurityConstants.REMOVE_CHILDREN)) {
					return true;
				}
			} catch (ClientException e) {
				return false;
			}
    	}
    	return false;
    }
    
    private static final SiteManager getSiteManager() {
    	try {
    		return Framework.getService(SiteManager.class);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static List<FontFamily> getFontFamilies(){
        return LabsSiteConstants.FontFamily.getFontFamilies();
    }
    
    public static List<FontSize> getFontSizes(){
        return LabsSiteConstants.FontSize.getFontSizes();
    }
}
