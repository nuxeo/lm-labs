package com.leroymerlin.common.core.providers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
public class DocumentModelListPageProviderTest {

    @Inject
    CoreSession session;
    
    @Test
    public void iCanGetPageProviderWith2Documents() throws Exception {
        DocumentModel file1 = session.createDocumentModel("/", "file1", "File");
        file1 = session.createDocument(file1);
        DocumentModel file2 = session.createDocumentModel("/", "file2", "File");
        file2 = session.createDocument(file2);
        session.save();
        DocumentModelList children = session.getChildren(new PathRef("/"), "File");
        assertFalse(children.isEmpty());
        assertEquals(2, children.size());
        DocumentModelListPageProvider pageProvider = new DocumentModelListPageProvider(children);
        pageProvider.setPageSize(0);
        List<DocumentModel> page0 = pageProvider.getCurrentPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        assertEquals(2, page0.size());
    }
    
    @Test
    public void iCanGetPages() throws Exception {
        DocumentModelList children = session.getChildren(new PathRef("/"), "File");
        assertFalse(children.isEmpty());
        DocumentModelListPageProvider pageProvider = new DocumentModelListPageProvider(children);
        pageProvider.setPageSize(1);
        List<DocumentModel> currentPage = pageProvider.getCurrentPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        assertEquals(2, pageProvider.getNumberOfPages());
        assertEquals(1, currentPage.size());
        pageProvider.nextPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        currentPage = pageProvider.getCurrentPage();
        assertEquals(1, currentPage.size());
        pageProvider.nextPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        currentPage = pageProvider.getCurrentPage();
        assertTrue(currentPage.isEmpty());
    }

    @Test
    public void iCanGetPages3Documents() throws Exception {
        DocumentModel file = session.createDocumentModel("/", "file3", "File");
        file = session.createDocument(file);
        session.save();
        DocumentModelList children = session.getChildren(new PathRef("/"), "File");
        assertFalse(children.isEmpty());
        DocumentModelListPageProvider pageProvider = new DocumentModelListPageProvider(children);
        pageProvider.setPageSize(2);
        List<DocumentModel> currentPage = pageProvider.getCurrentPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        assertEquals(2, pageProvider.getNumberOfPages());
        assertEquals(2, currentPage.size());
        pageProvider.nextPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        currentPage = pageProvider.getCurrentPage();
        assertEquals(1, currentPage.size());
        pageProvider.nextPage();
        assertEquals(children.size(), pageProvider.getResultsCount());
        currentPage = pageProvider.getCurrentPage();
        assertTrue(currentPage.isEmpty());
    }

}
