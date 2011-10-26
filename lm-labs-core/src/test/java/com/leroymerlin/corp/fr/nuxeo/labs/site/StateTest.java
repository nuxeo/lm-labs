package com.leroymerlin.corp.fr.nuxeo.labs.site;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy("com.leroymerlin.labs.core.test")
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
        DocumentModel page = createPageList(site1);
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
    public void iCanPublishAPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantDraftADraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.draft();
    }

    @Test
    public void iCanDraftAPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        adapter.draft();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    /**
     * @param site1
     * @return
     * @throws ClientException
     */
    private DocumentModel createPageList(DocumentModel site1) throws ClientException {
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "page",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        session.save();
        return page;
    }

    @Test
    public void iCantDisplayDraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        assertTrue(!adapter.isVisible());
    }

    @Test
    public void iCanDisplayPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        PageList adapter = page.getAdapter(PageList.class);
        adapter.publish();
        assertTrue(adapter.isVisible());
    }

    @Test
    public void iCanPublishADraftedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        
    }

    @Test
    public void iCanPDraftedAPublishedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).draft();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        
    }

    @Test(expected=ClientException.class)
    public void iCantPublishAPublishedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).publish();
        
    }

    @Test(expected=ClientException.class)
    public void iCantDraftADraftedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).draft();
        
    }

    @Test
    public void iCanDisplayPublishedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.publish();
        assertTrue(adapter.isVisible());
    }

    @Test
    public void iCantDisplayDraftedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        assertTrue(!adapter.isVisible());
    }

    @Test
    public void iCantDeleteADraftedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
    }

    @Test
    public void iCantDeleteAPublishedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantDeleteADeletedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
        adapter.delete();
    }

    @Test
    public void iCanUndeleteADeletedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
        adapter.undelete();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantUndeleteADraftedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.undelete();
    }

    @Test(expected=ClientException.class)
    public void iCantUndeleteAPublishedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        adapter.undelete();
    }
    
    @Test
    public void iCantDisplayADeletedSite() throws Exception {
        DocumentModel site1 = createSite();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        Page adapter = site1.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        assertTrue(adapter.isVisible());
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
        assertTrue(!adapter.isVisible());
    }
    
    
    
    

    @Test
    public void iCantDeleteADraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test
    public void iCantDeleteAPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantDeleteADeletedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
        adapter.delete();
    }

    @Test
    public void iCanUndeleteADeletedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
        adapter.undelete();
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }

    @Test(expected=ClientException.class)
    public void iCantUndeleteADraftedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.undelete();
    }

    @Test(expected=ClientException.class)
    public void iCantUndeleteAPublishedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        adapter.undelete();
    }
    
    @Test
    public void iCantDisplayADeletedPage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        assertTrue(adapter.isVisible());
        adapter.delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
        assertTrue(!adapter.isVisible());
    }
    
    @Test
    public void iRestaureOldStatePage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        Page adapter = page.getAdapter(Page.class);
        adapter.publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
        assertTrue(adapter.isVisible());
        //deleteSite
        site1.getAdapter(Page.class).delete();
        assertTrue(session.exists(page.getRef()));
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(page.getCurrentLifeCycleState()));
    }
    
    @Test
    public void iPublishOnlyThisSiteAndNotThePage() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = createPageList(site1);
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        //publish
        site1.getAdapter(Page.class).publish();
        assertTrue(LabsSiteConstants.State.PUBLISH.getState().equals(site1.getCurrentLifeCycleState()));
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(page.getCurrentLifeCycleState()));
    }
    
    @Test
    public void iDeletePageListWithoutDeletePageListLine() throws Exception {
        DocumentModel site1 = createSite();
        DocumentModel page = session.createDocumentModel(
                site1.getPathAsString(), "pageList",
                LabsSiteConstants.Docs.PAGELIST.type());
        page = session.createDocument(page);
        DocumentModel line = session.createDocumentModel(
                site1.getPathAsString(), "line",
                LabsSiteConstants.Docs.PAGELIST_LINE.type());
        line = session.createDocument(line);
        session.save();
        page.getAdapter(Page.class).delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(page.getCurrentLifeCycleState()));
        assertTrue(session.exists(line.getRef()));
        assertTrue(!LabsSiteConstants.State.DELETE.getState().equals(line.getCurrentLifeCycleState()));
        assertTrue(LabsSiteConstants.State.DRAFT.getState().equals(site1.getCurrentLifeCycleState()));
        site1.getAdapter(Page.class).delete();
        assertTrue(LabsSiteConstants.State.DELETE.getState().equals(site1.getCurrentLifeCycleState()));
        assertTrue(session.exists(line.getRef()));
        assertTrue(!LabsSiteConstants.State.DELETE.getState().equals(line.getCurrentLifeCycleState()));
    }
}
