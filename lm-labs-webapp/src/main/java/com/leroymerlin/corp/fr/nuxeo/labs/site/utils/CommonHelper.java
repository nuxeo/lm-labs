package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.operations.services.DocumentPageProviderOperation;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.page.CollapseTypes;
import com.leroymerlin.corp.fr.nuxeo.labs.site.services.LabsThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.FontFamily;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.FontSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.ThemePropertiesManage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

import edu.emory.mathcs.backport.java.util.Collections;

public final class CommonHelper {

    private static final Log LOG = LogFactory.getLog(CommonHelper.class);

    public CommonHelper() {
    }

    public static final SiteDocument siteDoc(DocumentModel doc) {
        return Tools.getAdapter(SiteDocument.class, doc, getCoreSession());
    }

    public static final Page sitePage(DocumentModel doc) throws ClientException {
        return sitePage(doc, getCoreSession());
    }

    public static final Page sitePage(DocumentModel doc, CoreSession session) throws ClientException {
        Page page = null;
        if (LabsSiteConstants.Docs.SITE.type().equals(doc.getType())) {
            DocumentModel homePage = Tools.getAdapter(LabsSite.class, doc, session).getIndexDocument();
            page = Tools.getAdapter(Page.class, homePage, session);
        } else {
            page = Tools.getAdapter(Page.class, doc, session);
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

    @SuppressWarnings("unchecked")
    public static final List<Page> getTopNavigationPages(DocumentModel siteDoc,
            final String userName) throws ClientException {
        List<Page> pages = new ArrayList<Page>();
        final CoreSession session = getCoreSession();
        LabsSite site = siteDoc(siteDoc).getSite();
        Collection<Page> allTopPages = siteDoc(site.getTree()).getChildrenPages();
        final DocumentModel homePageDoc = site.getIndexDocument();
        Page homePage = Tools.getAdapter(Page.class, homePageDoc, session);
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
                        boolean result = true;
                        if (!page.isContributor(userName)){
                            result = page.isVisible() && !page.isHiddenInNavigation();
                        }
                        return (result && !page.isDeleted());
                    } catch (ClientException e) {
                        return false;
                    }
                }
            }));
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
                }
            }));
        }
        return pages;
    }

    public static String quoteURIPathComponent(String s) {
        return URIUtils.quoteURIPathComponent(s, false);
    }

    public static final List<LabsSite> getTemplateSites()
            throws ClientException {
        SiteManager sm;
        List<LabsSite> adaptersList = new ArrayList<LabsSite>();
        try {
            sm = Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return adaptersList;
        }
        StringBuilder query = new StringBuilder();
        CoreSession session = getCoreSession();
        query.append("SELECT * FROM ").append(Docs.SITE.type()).append(
                " WHERE ").append(NXQL.ECM_LIFECYCLESTATE).append(" = '").append(
                State.PUBLISH.getState()).append("'").append(" AND ").append(
                NXQL.ECM_PATH).append(" STARTSWITH '").append(
                sm.getSiteRoot(session).getPathAsString().replace("'", "\\'")).append("'").append(
                " AND ").append(LabsSiteAdapter.PROPERTY_SITE_TEMPLATE).append(
                " = 1");
        DocumentModelList list = session.query(query.toString());
        for (DocumentModel site : list) {
            adaptersList.add(Tools.getAdapter(LabsSite.class, site, session));
        }
        return adaptersList;
    }

    public static final boolean canCreateSite(String principalId) {
        SiteManager sm = getSiteManager();
        if (sm != null) {
            try {
                DocumentModel siteRoot = sm.getSiteRoot(getCoreSession());
                if (getCoreSession().hasPermission(siteRoot.getRef(),
                        SecurityConstants.ADD_CHILDREN)) {
                    return true;
                }
            } catch (ClientException e) {
                return false;
            }
        }
        return false;
    }

    public static final boolean canCreateTemplateSite(String principalId) {
        SiteManager sm = getSiteManager();
        if (sm != null) {
            try {
                DocumentModel siteRoot = sm.getSiteRoot(getCoreSession());
                if (getCoreSession().hasPermission(siteRoot.getRef(),
                        SecurityConstants.EVERYTHING)) {
                    return true;
                }
            } catch (ClientException e) {
                return false;
            }
        }
        return false;
    }

    public static final boolean canDeleteSite(String principalId) {
        SiteManager sm = getSiteManager();
        if (sm != null) {
            try {
                DocumentModel siteRoot = sm.getSiteRoot(getCoreSession());
                if (getCoreSession().hasPermission(siteRoot.getRef(),
                        SecurityConstants.REMOVE_CHILDREN)) {
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

    public static boolean isNotRejectedComment(DocumentModel document){
        try {
            if (!LabsSiteConstants.CommentsState.REJECT.getState().equals(document.getCurrentLifeCycleState())){
                return true;
            }
            return false;
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    public static List<FontFamily> getFontFamilies() {
        List<FontFamily> list = new ArrayList<FontFamily>();
        Directories dir = Directories.FONT_FAMILIES;
        for (DocumentModel entry : getThemeService().getDirFontFamilies()) {
            try {
                list.add(new FontFamily((String) entry.getPropertyValue(dir.idField()), (String) entry.getPropertyValue(dir.labelField())));
            } catch (PropertyException e) {
                LOG.error(e, e);
            } catch (ClientException e) {
                LOG.error(e, e);
            }
        }
        return list;
    }

    public static List<FontSize> getFontSizes() {
        List<FontSize> list = new ArrayList<FontSize>();
        Directories dir = Directories.FONT_SIZES;
        for (DocumentModel entry : getThemeService().getDirFontSizes()) {
            try {
                list.add(new FontSize((String) entry.getPropertyValue(dir.idField()), (String) entry.getPropertyValue(dir.labelField())));
            } catch (PropertyException e) {
                LOG.error(e, e);
            } catch (ClientException e) {
                LOG.error(e, e);
            }
        }
        return list;
    }

    public static List<ThemeProperty> getThemeProperties(
            ThemePropertiesManage tpm) {
        List<ThemeProperty> properties = new ArrayList<ThemeProperty>();

        Map<String, ThemeProperty> props = tpm.getProperties();

        List<String> keys = new ArrayList<String>(props.keySet());
        Collections.sort(keys, new ThemePropertyComparator(props));

        for (String k : keys) {
            properties.add(props.get(k));
        }

        return properties;
    }

    public static List<String> getTemplates() {
        LabsThemeManager themeService = getThemeService();
        List<String> entriesTemplate = new ArrayList<String>();
        if (themeService != null) {
            entriesTemplate = themeService.getTemplateList(WebEngine.getActiveContext().getModule().getRoot().getAbsolutePath());
        }
        if (entriesTemplate.isEmpty()) {
            LOG.error("The themes should not be empty !");
            LOG.error("Verify the package " + LabsSiteWebAppUtils.DIRECTORY_THEME);
        }
        return entriesTemplate;
    }

    public static Calendar getNow() {
    	return Calendar.getInstance();
    }

    public DocumentModelList getPageProviderDocs(CoreSession session, final String providerName, final String queryParams, final int pageSize) {
        OperationContext ctx = new OperationContext(session);
		OperationChain chain = new OperationChain(providerName + "_" + session.getSessionId());
        chain.add(DocumentPageProviderOperation.ID).set("providerName", providerName).set("queryParams", queryParams).set("pageSize", new Integer(pageSize));
        try {
			return (DocumentModelList) Framework.getService(AutomationService.class).run(ctx, chain);
		} catch (Exception e) {
			LOG.error(e, e);
			return new DocumentModelListImpl();
		}
    }

    public String getPathAsString(DocumentModel doc) {
    	return doc.getPathAsString();
    }

    public List<String> getCollapseTypesList() {
    	List<String> list = new ArrayList<String>();
    	for (CollapseTypes type : CollapseTypes.values()) {
    		list.add(type.type());
    	}
    	return list;
    }
    /**
     * @throws Exception
     */
    private static LabsThemeManager getThemeService() {
        try {
            return Framework.getService(LabsThemeManager.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static CoreSession getCoreSession() {
        return WebEngine.getActiveContext()
                .getCoreSession();
    }
}
