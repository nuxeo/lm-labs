package com.leroymerlin.corp.fr.nuxeo.labs.site;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(user="Administrator")
public class StateTest {

    @Inject
    private CoreSession session;

    @Test
    public void iCreateSiteWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));

    }

    /**
     * @return
     * @throws ClientException
     */
    private DocumentModel createSite() throws ClientException {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
        assertTrue(session.exists(sitesRoot.getRef()));
        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        site1 = session.createDocument(site1);
        return site1;
    }

    @Test
    public void iCreatePageClasseurWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "pageClasseur",
                LabsSiteConstants.Docs.PAGECLASSEUR.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreatePageBlocWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "pageBloc",
                LabsSiteConstants.Docs.PAGEBLOCS.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreatePageListWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreatePageNewsWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGENEWS.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreatePageHTMLWithInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.HTMLPAGE.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreatePageListLineWithoutInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST_LINE.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(!LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCreateFolderWithoutInitialState() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST_LINE.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(!LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCanPublishAPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantDraftADraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.draft();
    }

    @Test
    public void iCanDraftAPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        adapter.draft();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCantDisplayDraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        assertTrue(!adapter.isVisible());
    }

    @Test
    public void iCanDisplayPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(adapter.isVisible());
    }
    
}
