/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;

import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.corp.fr.nuxeo.labs.filter.DocUnderVisiblePageFilter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageSubscription;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManagerImpl;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Rights;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.piwik.Piwik;

/**
 * @author fvandaele
 * 
 */
public class LabsSiteAdapter extends AbstractLabsBase implements LabsSite {

    public static final int NB_LAST_UPDATED_DOCS = 20;

    public static final int NB_LAST_UPDATED_NEWS_DOCS = 20;

    public static final String PROPERTY_SITE_TEMPLATE = Schemas.LABSSITE.prefix()
            + ":siteTemplate";

    private static final String PROPERTY_HOME_PAGE_REF = Schemas.LABSSITE.prefix()
            + ":homePageRef";

    private static final String PROPERTY_SITE_TEMPLATE_PREVIEW = Schemas.LABSSITE.prefix()
            + ":siteTemplatePreview";

    private static final String PROPERTY_CONTACTS = Schemas.LABSSITE.prefix()
            + ":contacts";

    private static final String PROPERTY_URL = "webcontainer:url";

    private static final Log LOG = LogFactory.getLog(LabsSiteAdapter.class);

    public LabsSiteAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public String getURL() throws ClientException {
        return (String) doc.getPropertyValue(PROPERTY_URL);
    }

    @Override
    public void setURL(String pURL) throws ClientException {
        doc.setPropertyValue(PROPERTY_URL, pURL);
    }

    @Override
    public void setDescription(String description) throws PropertyException,
            ClientException {
        if (description == null) {
            return;
        }
        doc.setPropertyValue("dc:description", description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:description");
    }

    @Override
    public Blob getBanner() throws ClientException {
        return doc.getAdapter(LabsSite.class).getThemeManager().getTheme().getBanner();
    }

    @Override
    public void setBanner(Blob pBlob) throws ClientException {
        SiteTheme theme = doc.getAdapter(LabsSite.class).getThemeManager().getTheme();
        if (pBlob == null) {
            theme.setBanner(null);
            doc.getCoreSession().saveDocument(theme.getDocument());
        } else {
            theme.setBanner(pBlob);
            doc.getCoreSession().saveDocument(theme.getDocument());
        }
    }

    @Override
    public List<Page> getAllPages() throws ClientException {
        DocumentModelList docs = getCoreSession().query(
                "SELECT * FROM Page, Space where ecm:currentLifeCycleState <> 'deleted' AND ecm:path STARTSWITH '"
                        + getTree().getPathAsString().replace("'", "\\'") + "'");

        List<Page> pages = new ArrayList<Page>();
        for (DocumentModel doc : docs) {
            Page page = doc.getAdapter(Page.class);
            if (page != null) {
                pages.add(page);
            }
        }
        return pages;
    }

    // TODO unit tests
    @Override
    public Collection<DocumentModel> getPages(Docs docType, State lifecycleState)
            throws ClientException {
        String docTypeStr = Docs.PAGE.type() + ", " + Docs.DASHBOARD.type();
        if (docType != null) {
            docTypeStr = Docs.PAGE.type();
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(docTypeStr).append(" WHERE ").append(
                NXQL.ECM_PATH).append(" STARTSWITH ").append("'").append(
                doc.getPathAsString().replace("'", "\\'")).append("'");
        if (lifecycleState != null) {
            query.append(" AND ").append(NXQL.ECM_LIFECYCLESTATE).append(" = '").append(
                    lifecycleState.getState()).append("'");
        } else {
            query.append(NXQL.ECM_LIFECYCLESTATE).append(" <> 'deleted'");
        }
        return getCoreSession().query(query.toString());
    }

    @Override
    public List<Page> getAllDeletedPages() throws ClientException {
        DocumentModelList docs = getCoreSession().query(
                "SELECT * FROM Page, Space where ecm:currentLifeCycleState = 'deleted' AND ecm:path STARTSWITH '"
                        + doc.getPathAsString().replace("'", "\\'") + "'");

        List<Page> pages = new ArrayList<Page>();
        for (DocumentModel doc : docs) {
            Page page = doc.getAdapter(Page.class);
            if (page != null) {
                pages.add(page);
            }
        }
        return pages;
    }

    @Override
    public DocumentModelList getAllDeletedDocs() throws ClientException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document").append(" WHERE ").append(
                NXQL.ECM_LIFECYCLESTATE).append(" = '").append(
                LifeCycleConstants.DELETED_STATE).append("'").append(" AND ").append(
                NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString().replace("'", "\\'")).append("'");
        DocumentModelList docs = getCoreSession().query(query.toString(),
                new Filter() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean accept(DocumentModel arg0) {
                        if (Docs.pageDocs().contains(
                                Docs.fromString(arg0.getType()))) {
                            return true;
                        } else if (Docs.PAGECLASSEURFOLDER.type().equals(
                                arg0.getType())) {
                            try {
                                DocumentModel parent = arg0.getCoreSession().getParentDocument(
                                        arg0.getRef());
                                if (Docs.PAGECLASSEUR.type().equals(
                                        parent.getType())) {
                                    return true;
                                }
                            } catch (ClientException e) {
                                return false;
                            }
                            return false;
                        } else {
                            try {
                                DocumentModel parent = arg0.getCoreSession().getParentDocument(
                                        arg0.getRef());
                                DocumentModel grandParent = arg0.getCoreSession().getDocument(
                                        parent.getParentRef());
                                return Docs.PAGECLASSEURFOLDER.type().equals(
                                        parent.getType())
                                        && Docs.pageDocs().contains(
                                                Docs.fromString(grandParent.getType()));
                            } catch (ClientException e) {
                                LOG.error(e, e);
                            }
                            return false;
                        }
                    }
                });
        return docs;
    }

    private CoreSession getCoreSession() {
        return doc.getCoreSession();
    }

    @Override
    public DocumentModel getTree() throws ClientException {
        return getCoreSession().getChild(doc.getRef(), Docs.TREE.docName());
    }

    public static DocumentModel getDefaultRoot(CoreSession coreSession) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LabsSite)) {
            return false;
        }
        return getDocument().getId().equals(
                ((LabsSite) obj).getDocument().getId());
    }

    @Override
    public int hashCode() {
        return getDocument().getId().hashCode();
    }

    @Override
    public String toString() {
        String url = "";
        try {
            url = getURL();
        } catch (ClientException e) {
            url = "ClientException : Url not found";
        }
        return String.format("LabsSite at %s (url: %s)", doc.getPathAsString(),
                url);
    }

    @Override
    public SiteThemeManager getThemeManager() throws ClientException {
        return new SiteThemeManagerImpl(doc);
    }

    @Override
    public DocumentModel getIndexDocument() throws ClientException {
        return getCoreSession().getDocument(new IdRef(getHomePageRef()));
    }

    @Override
    public String[] getAllowedSubtypes() throws ClientException {
        return getAllowedSubtypes(getTree());
    }

    @Override
    public DocumentModel getAssetsDoc() throws ClientException {
        return getCoreSession().getChild(doc.getRef(), Docs.ASSETS.docName());
    }

    @Override
    public void setHomePageRef(String homePageRef) throws ClientException {
        doc.setPropertyValue(PROPERTY_HOME_PAGE_REF, homePageRef);
    }

    @Override
    public String getHomePageRef() throws ClientException {
        // return (String) doc.getPropertyValue(HOME_PAGE_REF);
        try {
            String ref = (String) doc.getPropertyValue(PROPERTY_HOME_PAGE_REF);
            // TODO this is temporary
            if (StringUtils.isEmpty(ref)) {
                ref = getWelcomePageId();
            }
            if (doc.getId().equals(ref)) {
                LOG.warn("Wrong home page.");
            }
            return ref;
        } catch (Exception e) {
            LOG.error("Unable to get home page for site '" + getURL() + "': " + e);
            // TODO this is temporary
            return getWelcomePageId();
        }
    }

    private String getWelcomePageId() throws ClientException {
        if (doc.getCoreSession().hasPermission(doc.getRef(),
                SecurityConstants.WRITE)) {
            PathRef ref = new PathRef(doc.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName(),
                    LabsSiteConstants.Docs.WELCOME.docName());
            DocumentModel welcome = doc.getCoreSession().getDocument(ref);
            setHomePageRef(welcome.getId());
            doc.getCoreSession().saveDocument(doc);
            doc.getCoreSession().save();
            return (String) doc.getPropertyValue(PROPERTY_HOME_PAGE_REF);
        }
        throw WebException.wrap(new WebResourceNotFoundException(
                "home page not set."));
    }

    @Override
    public DocumentModelList getLastUpdatedDocs() throws ClientException {
        StringBuilder query = new StringBuilder("SELECT * FROM Document");
        query.append(" WHERE ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString().replace("'", "\\'")).append("/").append(
                LabsSiteConstants.Docs.TREE.docName()).append("'");
        query.append(" AND ecm:isCheckedInVersion = 0");
        query.append(" AND ecm:currentLifeCycleState <> 'deleted'");
        query.append(" AND ").append(NXQL.ECM_MIXINTYPE).append(
                " <> 'HiddenInNavigation'");
        query.append(" ORDER BY dc:modified DESC");

        final DocUnderVisiblePageFilter docUnderVisiblePageFilter = new DocUnderVisiblePageFilter();
        return getCoreSession().query(query.toString(), new Filter() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean accept(DocumentModel doc) {
                return docUnderVisiblePageFilter.accept(doc);
            }
        }, NB_LAST_UPDATED_DOCS);
    }

    @Override
    public DocumentModelList getLastUpdatedNewsDocs() throws ClientException {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(Docs.LABSNEWS.type());
        query.append(" WHERE ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString().replace("'", "\\'")).append("/").append(
                LabsSiteConstants.Docs.TREE.docName()).append("'");
        query.append(" AND ecm:isCheckedInVersion = 0");
        query.append(" AND ecm:currentLifeCycleState <> 'deleted'");
        query.append(" AND ").append(NXQL.ECM_MIXINTYPE).append(
                " <> 'HiddenInNavigation'");

        final DocUnderVisiblePageFilter docUnderVisiblePageFilter = new DocUnderVisiblePageFilter();
        return getCoreSession().query(query.toString(), new Filter() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean accept(DocumentModel doc) {
                return docUnderVisiblePageFilter.accept(doc);
            }
        }, NB_LAST_UPDATED_NEWS_DOCS);
    }

    @Override
    public ArrayList<ExternalURL> getExternalURLs() throws ClientException {
        ArrayList<ExternalURL> listExtURL = new ArrayList<ExternalURL>();
        if (getCoreSession().exists(
                new PathRef(doc.getPathAsString() + "/"
                        + Docs.EXTERNAL_URLS.docName()))) {
            DocumentModel folder = getCoreSession().getDocument(
                    new PathRef(doc.getPathAsString() + "/"
                            + Docs.EXTERNAL_URLS.docName()));
            DocumentModelList listDoc = doc.getCoreSession().getChildren(
                    folder.getRef(),
                    LabsSiteConstants.Docs.EXTERNAL_URL.type(), null, null,
                    null);
            for (DocumentModel urlDoc : listDoc) {
                ExternalURL extURL = urlDoc.getAdapter(ExternalURL.class);
                listExtURL.add(extURL);
            }
        }
        return listExtURL;
    }

    @Override
    public ExternalURL createExternalURL(String title) throws ClientException {
        DocumentModel folder;
        if (!getCoreSession().exists(
                new PathRef(doc.getPathAsString() + "/"
                        + Docs.EXTERNAL_URLS.docName()))) {
            folder = getCoreSession().createDocumentModel(
                    doc.getPathAsString(), Docs.EXTERNAL_URLS.docName(),
                    Docs.EXTERNAL_URLS.type());
            folder = getCoreSession().createDocument(folder);
        } else {
            folder = getCoreSession().getDocument(
                    new PathRef(doc.getPathAsString() + "/"
                            + Docs.EXTERNAL_URLS.docName()));
        }
        DocumentModel docExtURL = getCoreSession().createDocumentModel(
                folder.getPathAsString(), title,
                LabsSiteConstants.Docs.EXTERNAL_URL.type());
        ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
        extURL.setName(title);
        getCoreSession().createDocument(docExtURL);
        return extURL;
    }

    // TODO vdu unit test
    @Override
    public boolean updateUrls(String oldUrl, String newUrl)
            throws ClientException {
        List<String> stringUrlFields = new ArrayList<String>(
                Arrays.asList("dc:description"));
        StringBuilder queryFormat = new StringBuilder();
        queryFormat.append("SELECT * FROM ").append(Docs.PAGE.type()).append(
                " WHERE ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString().replace("'", "\\'")).append("'").append(" AND ").append("%s").append(
                " LIKE '%%").append(oldUrl).append("%%'");
        String queryFormatStr = queryFormat.toString();

        boolean anyUpdated = false;
        for (String fieldName : stringUrlFields) {
            boolean updated = false;
            String query = String.format(queryFormatStr, fieldName);
            DocumentModelList docs = getCoreSession().query(query);
            for (DocumentModel document : docs) {
                updated = updateUrlInProperty(document, fieldName, oldUrl,
                        newUrl);
                if (updated) {
                    getCoreSession().saveDocument(document);
                }
            }
            anyUpdated |= updated;
        }

        // TODO since 5.5.0 you can query complex properties
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(
                StringUtils.join(new String[] { Docs.HTMLPAGE.type(),
                        Docs.LABSNEWS.type() }, ",")).append(" WHERE ").append(
                NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString()).append("'");
        DocumentModelList docs = getCoreSession().query(query.toString());
        for (DocumentModel document : docs) {
            boolean updated = false;
            if (Docs.HTMLPAGE.type().equals(document.getType())) {
                HtmlPage htmlPage = document.getAdapter(HtmlPage.class);
                for (HtmlSection section : htmlPage.getSections()) {
                    updated = processRows(section.getRows(), oldUrl, newUrl);
                }
            } else { // LabsNews
                LabsNews news = document.getAdapter(LabsNews.class);
                updated = processRows(news.getRows(), oldUrl, newUrl);
            }
            if (updated) {
                getCoreSession().saveDocument(document);
            }
            anyUpdated |= updated;
        }
        return anyUpdated;
    }

    private boolean processRows(List<HtmlRow> rows, String oldUrl, String newUrl)
            throws ClientException {
        boolean updated = false;
        for (HtmlRow row : rows) {
            for (HtmlContent content : row.getContents()) {
                String html = content.getHtml();
                if (html.contains(oldUrl)) {
                    html = StringUtils.replace(html, oldUrl, newUrl);
                    content.setHtml(html);
                    updated = true;
                }
            }
        }
        return updated;
    }

    private boolean updateUrlInProperty(DocumentModel document,
            String propertyName, String oldUrl, String newUrl)
            throws ClientException {
        boolean updated = false;
        String stringField = (String) document.getPropertyValue(propertyName);
        if (stringField.contains(oldUrl)) {
            stringField = StringUtils.replace(stringField, oldUrl, newUrl);
            document.setPropertyValue(propertyName, stringField);
            updated = true;
        }
        return updated;
    }

    @Override
    public List<String> getAdministratorsSite() throws Exception {
        List<String> result = new ArrayList<String>();
        List<LMPermission> permissions = LabsSiteUtils.extractPermissions(this.doc);
        for (LMPermission perm : permissions) {
            if (Rights.EVERYTHING.getRight().equals(perm.getPermission())) {
                result.add(perm.getName());
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getContacts() throws Exception {
        return (List<String>) getDocument().getPropertyValue(PROPERTY_CONTACTS);
    }

    @Override
    public boolean addContact(String ldap) throws Exception {
        List<String> contacts = getContacts();
        if (!contacts.contains(ldap)) {
            CoreSession session = getDocument().getCoreSession();
            DocumentModel doc = getDocument();
            contacts.add(ldap);
            doc.setPropertyValue(PROPERTY_CONTACTS, (Serializable) contacts);
            session.saveDocument(doc);
            session.save();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteContact(String ldap) throws Exception {
        List<String> contacts = getContacts();
        if (contacts.contains(ldap)) {
            CoreSession session = getDocument().getCoreSession();
            DocumentModel doc = getDocument();
            contacts.remove(ldap);
            doc.setPropertyValue(PROPERTY_CONTACTS, (Serializable) contacts);
            session.saveDocument(doc);
            session.save();
            return true;
        }
        return false;
    }

    @Override
    public String getPiwikId() throws ClientException {
        return doc.getAdapter(Piwik.class).getId();
    }

    @Override
    public void setPiwikId(String piwikId) throws ClientException {
        doc.getAdapter(Piwik.class).setId(piwikId);
    }

    @Override
    public boolean isPiwikEnabled() throws ClientException {
        return StringUtils.isNotBlank(getPiwikId());
    }

    @Override
    public void setSiteTemplate(boolean value) throws ClientException {
        doc.setPropertyValue(PROPERTY_SITE_TEMPLATE, new Boolean(value));
    }

    @Override
    public boolean isSiteTemplate() throws ClientException {
        return (Boolean) doc.getPropertyValue(PROPERTY_SITE_TEMPLATE);
    }

    @Override
    public Blob getSiteTemplatePreview() throws ClientException {
        return (Blob) doc.getPropertyValue(PROPERTY_SITE_TEMPLATE_PREVIEW);
    }

    @Override
    public void setSiteTemplatePreview(Blob blob) throws ClientException {
        doc.setPropertyValue(PROPERTY_SITE_TEMPLATE_PREVIEW,
                (Serializable) blob);
    }

    @Override
    public boolean hasSiteTemplatePreview() throws ClientException {
        return (getSiteTemplatePreview() != null);
    }

    @Override
    public void applyTemplateSite(final DocumentModel templateSite)
            throws ClientException, IllegalArgumentException {
        final String templateThemeName = (String) templateSite.getPropertyValue(Schemas.LABSSITE.prefix()
                + ":theme_name");
        UnrestrictedSessionRunner sessionRunner = new UnrestrictedSessionRunner(
                templateSite.getCoreSession()) {
            @Override
            public void run() throws ClientException {
                LabsSite labsSiteTemplate = templateSite.getAdapter(LabsSite.class);
                if (labsSiteTemplate.isSiteTemplate()) {
                    Path indexLastSegments = labsSiteTemplate.getIndexDocument().getPath().removeFirstSegments(
                            templateSite.getPath().segmentCount());
                    session.removeDocuments(session.getChildrenRefs(
                            doc.getRef(), null).toArray(new DocumentRef[] {}));
                    session.copy(
                            session.getChildrenRefs(templateSite.getRef(), null),
                            doc.getRef());
                    Path indexPath = doc.getPath().append(indexLastSegments);
                    setHomePageRef(session.getDocument(
                            new PathRef(indexPath.toString())).getId());
                    setThemeName(templateThemeName);
                    getTemplate().setTemplateName(labsSiteTemplate.getTemplate().getTemplateName());
                    // TODO it looks like Nuxeo does NOT copy schemas of dynamically added facets !!!
                    copyFacetSchemas(doc, templateSite, session);
                } else {
                    throw new IllegalArgumentException(
                            templateSite.getPathAsString()
                                    + " is not a site template.");
                }

            }
        };
        sessionRunner.runUnrestricted();
    }

    protected void copyFacetSchemas(DocumentModel site, final DocumentModel templateSite, CoreSession session) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(Docs.PAGE.type())
            .append(" WHERE ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(templateSite.getPathAsString()).append("'")
            .append(" AND ").append(NXQL.ECM_MIXINTYPE).append(" = '").append(FacetNames.LABSTEMPLATE).append("'");
        try {
            DocumentModelList facetedDocs = session.query(query.toString());
            for(DocumentModel facetedDoc : facetedDocs) {
                String endPath = StringUtils.substringAfter(facetedDoc.getPathAsString(), templateSite.getPathAsString());
                DocumentModel document = session.getDocument(new PathRef(site.getPathAsString() + endPath));
                document.addFacet(FacetNames.LABSTEMPLATE);
                document.setProperties(Schemas.LABSTEMPLATE.getName(), facetedDoc.getProperties(Schemas.LABSTEMPLATE.getName()));
                session.saveDocument(document);
            }
        } catch (ClientException e) {
            LOG.error(e, e);
        }
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setThemeName(String name) throws ClientException {
        doc.setPropertyValue(Schemas.LABSSITE.prefix() + ":theme_name", name);
    }

    @Override
    public String getThemeName() throws ClientException {
        return (String) doc.getPropertyValue(Schemas.LABSSITE.prefix()
                + ":theme_name");
    }

    @Override
    public List<Page> getSubscribedPages() throws ClientException {
        List<Page> subscribedPages = new ArrayList<Page>();
        for (Page page : getAllPages()) {
            try {
                PageSubscription adapter = page.getDocument().getAdapter(
                        PageSubscription.class);
                if (adapter != null
                        && adapter.isSubscribed(getCoreSession().getPrincipal().getName())) {
                    subscribedPages.add(page);
                }
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
        return subscribedPages;
    }
}
