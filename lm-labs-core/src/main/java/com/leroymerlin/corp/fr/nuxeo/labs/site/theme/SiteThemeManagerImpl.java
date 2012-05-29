package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class SiteThemeManagerImpl implements SiteThemeManager {

    private final DocumentModel parentSiteDoc;

    public SiteThemeManagerImpl(DocumentModel doc) {
        this.parentSiteDoc = doc;
    }

    private DocumentModel getRootOfThemes(CoreSession session) throws ClientException {
        return session.getDocument(new PathRef(getThemesRootPath()));
    }

    private String getThemesRootPath() {
    	return parentSiteDoc.getPathAsString() + "/" + Docs.SITETHEMESROOT.docName();
    }

    @Override
    public SiteTheme getTheme(String themeName, CoreSession session) throws ClientException {
        DocumentRef themeRef = new PathRef(getThemesRootPath() + "/" + themeName);
        if (session.exists(themeRef)) {
            return session.getDocument(themeRef)
                    .getAdapter(SiteTheme.class);
        } else {
            return null;
        }

    }

    @Override
    public SiteTheme getTheme(CoreSession session) throws ClientException {
        String themeName = (String) parentSiteDoc.getPropertyValue(Schemas.LABSSITE.prefix() + ":theme_name");
        SiteTheme theme = getTheme(themeName, session);

        if (theme == null) {
            theme = createTheme(themeName, session);
        }
        return theme;
    }

    private SiteTheme createTheme(final String themeName, CoreSession session) throws ClientException {
        UnrestrictedSessionRunner sessionRunner = new UnrestrictedSessionRunner(session) {
            @Override
            public void run() throws ClientException {
            	DocumentModel rootOfThemes = getRootOfThemes(session);
				DocumentRef themeRef = new PathRef(rootOfThemes.getPathAsString()
            			+ "/" + themeName);
                if (!session.exists(themeRef)) {
                    DocumentModel themeDoc = session.createDocumentModel(
                            rootOfThemes.getPathAsString(), themeName, Docs.SITETHEME.type());
                    themeDoc = session.createDocument(themeDoc);
                    session.save();
                }
            }
        };
        sessionRunner.runUnrestricted();
        DocumentModel themeDoc = session.getDocument(new PathRef(getThemesRootPath() + "/" + themeName));
        return themeDoc.getAdapter(SiteTheme.class);
    }

    @Override
    public void setTheme(String themeName, CoreSession session) throws ClientException {
        if (getTheme(themeName, session) == null){
            createTheme(themeName, session);
        }
        parentSiteDoc.setPropertyValue(Schemas.LABSSITE.prefix() + ":theme_name", themeName);
        session.saveDocument(parentSiteDoc);
        session.save();
    }

    @Override
    public List<SiteTheme> getThemes(CoreSession session) throws ClientException {
        // TODO Auto-generated method stub
        return null;
    }
}
