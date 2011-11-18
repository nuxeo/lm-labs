package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class SiteThemeManagerImpl implements SiteThemeManager {

    private final DocumentModel parentSiteDoc;

    public SiteThemeManagerImpl(DocumentModel doc) {
        this.parentSiteDoc = doc;
    }

    private DocumentModel getRootOfThemes() throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();
        DocumentRef rootRef = new PathRef(parentSiteDoc.getPathAsString()
                + "/themes");
        if (session.exists(rootRef)) {
            return session.getDocument(rootRef);
        } else {
            return createRoot();
        }

    }

    private DocumentModel createRoot() throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();
        DocumentModel rootDoc = session.createDocumentModel(
                parentSiteDoc.getPathAsString(), "themes", "Folder");
        rootDoc.setPropertyValue("dc:title", "themes");
        rootDoc = session.createDocument(rootDoc);
        session.save();
        return rootDoc;
    }



    @Override
    public SiteTheme getTheme(String themeName) throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();

        DocumentRef themeRef = new PathRef(getRootOfThemes().getPathAsString()
                + "/" + themeName);
        if (session.exists(themeRef)) {
            return session.getDocument(themeRef)
                    .getAdapter(SiteTheme.class);
        } else {
            return null;
        }

    }

    @Override
    public SiteTheme getTheme() throws ClientException {
        String themeName = (String) parentSiteDoc.getPropertyValue(Schemas.LABSSITE.prefix() + ":theme_name");
        SiteTheme theme = getTheme(themeName);

        if (theme == null) {
            theme = createTheme(themeName);
        }
        return theme;
    }

    private SiteTheme createTheme(String themeName) throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();
        DocumentModel themeDoc = session.createDocumentModel(
                getRootOfThemes().getPathAsString(), themeName, Docs.SITETHEME.type());
        themeDoc = session.createDocument(themeDoc);
        session.save();
        return themeDoc.getAdapter(SiteTheme.class);
    }

    @Override
    public void setTheme(String themeName) throws ClientException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<SiteTheme> getThemes() throws ClientException {
        // TODO Auto-generated method stub
        return null;
    }
}
