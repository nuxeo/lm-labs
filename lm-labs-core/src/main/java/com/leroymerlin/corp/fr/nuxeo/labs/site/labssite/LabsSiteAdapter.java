/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.ExternalURLSorter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManagerImpl;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

/**
 * @author fvandaele
 * 
 */
public class LabsSiteAdapter extends AbstractLabsBase implements LabsSite {

    public static final int NB_LAST_UPDATED_DOCS = 20;

    private static final String HOME_PAGE_REF = "labssite:homePageRef";

    static final String URL = "webcontainer:url";

    public LabsSiteAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public String getURL() throws ClientException {
        return (String) doc.getPropertyValue(URL);
    }

    @Override
    public void setURL(String pURL) throws ClientException {
        doc.setPropertyValue(URL, pURL);
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
                        + doc.getPathAsString() + "'");

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
                doc.getPathAsString()).append("'");
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
                        + doc.getPathAsString() + "'");

        List<Page> pages = new ArrayList<Page>();
        for (DocumentModel doc : docs) {
            Page page = doc.getAdapter(Page.class);
            if (page != null) {
                pages.add(page);
            }
        }
        return pages;
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
        doc.setPropertyValue(HOME_PAGE_REF, homePageRef);
    }

    @Override
    public String getHomePageRef() throws ClientException {
        // return (String) doc.getPropertyValue(HOME_PAGE_REF);
        try {
            String ref = (String) doc.getPropertyValue(HOME_PAGE_REF);
            // TODO this is temporary
            if (StringUtils.isEmpty(ref)) {
                return getWelcomePageId();
            }
            return ref;
        } catch (Exception e) {
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
            return (String) doc.getPropertyValue(HOME_PAGE_REF);
        }
        throw WebException.wrap(new WebResourceNotFoundException(
                "home page not set."));
    }

    @Override
    public DocumentModelList getLastUpdatedDocs() throws ClientException {
        StringBuilder query = new StringBuilder("SELECT * FROM Document");
        query.append(" WHERE ").append(NXQL.ECM_PATH).append(" STARTSWITH '").append(
                doc.getPathAsString()).append("/").append(
                LabsSiteConstants.Docs.TREE.docName()).append("'");
        query.append(" AND ecm:isCheckedInVersion = 0");
        query.append(" AND ecm:currentLifeCycleState <> 'deleted'");
        query.append(" AND ").append(NXQL.ECM_MIXINTYPE).append(" <> 'HiddenInNavigation'");
        query.append(" ORDER BY dc:modified DESC");

        return getCoreSession().query(query.toString(), NB_LAST_UPDATED_DOCS);
    }

    @Override
    public ArrayList<ExternalURL> getExternalURLs() throws ClientException {
        ArrayList<ExternalURL> listExtURL = new ArrayList<ExternalURL>();
        if (getCoreSession().exists(new PathRef(doc.getPathAsString() + "/" + Docs.EXTERNAL_URLS.docName()))) {
            DocumentModel folder = getCoreSession().getDocument(new PathRef(doc.getPathAsString() + "/" + Docs.EXTERNAL_URLS.docName()));
            DocumentModelList listDoc = doc.getCoreSession().getChildren(folder.getRef(),
                    LabsSiteConstants.Docs.EXTERNAL_URL.type(), null, null,
                    new ExternalURLSorter());
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
        if (!getCoreSession().exists(new PathRef(doc.getPathAsString() + "/" + Docs.EXTERNAL_URLS.docName()))) {
            folder = getCoreSession().createDocumentModel(doc.getPathAsString(), Docs.EXTERNAL_URLS.docName(), Docs.EXTERNAL_URLS.type());
            folder = getCoreSession().createDocument(folder);
        } else {
            folder = getCoreSession().getDocument(new PathRef(doc.getPathAsString() + "/" + Docs.EXTERNAL_URLS.docName()));
        }
        DocumentModel docExtURL = getCoreSession().createDocumentModel(
                folder.getPathAsString(), title,
                LabsSiteConstants.Docs.EXTERNAL_URL.type());
        ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
        extURL.setName(title);
        getCoreSession().createDocument(docExtURL);
        return extURL;
    }

}
