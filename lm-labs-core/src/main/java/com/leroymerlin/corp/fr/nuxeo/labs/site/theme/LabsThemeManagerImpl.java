package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.LabsTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class LabsThemeManagerImpl implements LabsThemeManager {

    private final DocumentModel parentSiteDoc;

    public LabsThemeManagerImpl(DocumentModel doc) {
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
    public LabsTheme getTheme(String themeName) throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();

        DocumentRef themeRef = new PathRef(getRootOfThemes().getPathAsString()
                + "/" + themeName);
        if (session.exists(themeRef)) {
            return session.getDocument(themeRef)
                    .getAdapter(LabsTheme.class);
        } else {
            return null;
        }

    }

    @Override
    public LabsTheme getTheme() throws ClientException {
        String themeName = (String) parentSiteDoc.getPropertyValue("labssite:theme_name");
        LabsTheme theme = getTheme(themeName);

        if (theme == null) {
            theme = createTheme(themeName);
        }
        return theme;
    }

    private LabsTheme createTheme(String themeName) throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();
        DocumentModel themeDoc = session.createDocumentModel(
                getRootOfThemes().getPathAsString(), themeName, Docs.LABSTHEME.type());
        themeDoc = session.createDocument(themeDoc);
        session.save();
        return themeDoc.getAdapter(LabsTheme.class);
    }

    @Override
    public void setTheme(String themeName) throws ClientException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<LabsTheme> getThemes() throws ClientException {
        // TODO Auto-generated method stub
        return null;
    }
}
