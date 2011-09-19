/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

/**
 * @author fvandaele
 *
 */
public class LabsSiteAdapter extends AbstractPage implements LabsSite {

    static final String URL = "webcontainer:url";

    static final String BANNER = "webcontainer:logo";

    public LabsSiteAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public DocumentModel getDocumentModel() {
        return doc;
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
    public Blob getLogo() throws ClientException {
        return (Blob) doc.getPropertyValue(BANNER);
    }

    @Override
    public void setLogo(Blob pBlob) throws ClientException {
        if (pBlob == null) {
            doc.setPropertyValue(BANNER, null);
        } else {
            doc.setPropertyValue(BANNER, (Serializable) pBlob);
        }
    }

    @Override
    public List<Page> getAllPages() throws ClientException {
        DocumentModelList docs = getCoreSession().query(
                "SELECT * FROM Page where ecm:path STARTSWITH '"
                        + doc.getPathAsString() + "'");

        List<Page> pages = new ArrayList<Page>();
        for (DocumentModel doc : docs) {
            pages.add(doc.getAdapter(Page.class));
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
        return getDocument().getId()
                .equals(((LabsSite) obj).getDocument()
                        .getId());
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
        return String.format("LabsSite at %s (url: %s)", doc.getPathAsString(), url);
    }

    @Override
    public SiteThemeManager getSiteThemeManager() throws ClientException {
        return new SiteThemeManagerImpl(doc);
    }





}
