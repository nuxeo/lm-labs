package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

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
        final DocumentRef rootRef = new PathRef(parentSiteDoc.getPathAsString()
                + "/themes");
        CoreSession session = parentSiteDoc.getCoreSession();
        UnrestrictedSessionRunner sessionRunner = new UnrestrictedSessionRunner(session) {
            @Override
            public void run() throws ClientException {
                if (!session.exists(rootRef)) {
                    DocumentModel rootDoc = session.createDocumentModel(
                            parentSiteDoc.getPathAsString(), "themes", "Folder");
                    rootDoc.setPropertyValue("dc:title", "themes");
                    rootDoc = session.createDocument(rootDoc);
                    session.save();
                }
            }
        };
        sessionRunner.runUnrestricted();
        return session.getDocument(rootRef);
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

    private SiteTheme createTheme(final String themeName) throws ClientException {
        CoreSession session = parentSiteDoc.getCoreSession();
        final DocumentRef themeRef = new PathRef(getRootOfThemes().getPathAsString()
                + "/" + themeName);
        UnrestrictedSessionRunner sessionRunner = new UnrestrictedSessionRunner(session) {
            @Override
            public void run() throws ClientException {
                if (!session.exists(themeRef)) {
                    DocumentModel themeDoc = session.createDocumentModel(
                            getRootOfThemes().getPathAsString(), themeName, Docs.SITETHEME.type());
                    themeDoc = session.createDocument(themeDoc);
                    session.save();
                }
            }
        };
        sessionRunner.runUnrestricted();
        DocumentModel themeDoc = session.getDocument(themeRef);
        return themeDoc.getAdapter(SiteTheme.class);
    }

    @Override
    public void setTheme(String themeName) throws ClientException {
        if (getTheme(themeName) == null){
            createTheme(themeName);
        }
        parentSiteDoc.setPropertyValue(Schemas.LABSSITE.prefix() + ":theme_name", themeName);
        CoreSession session = parentSiteDoc.getCoreSession();
        session.saveDocument(parentSiteDoc);
        session.save();
    }

    @Override
    public List<SiteTheme> getThemes() throws ClientException {
        // TODO Auto-generated method stub
        return null;
    }
}
