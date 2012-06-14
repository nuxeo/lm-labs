package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "org.nuxeo.ecm.platform.query.api",
        "org.nuxeo.ecm.platform.types.api",
        "org.nuxeo.ecm.platform.types.core",
        "org.nuxeo.ecm.platform.mimetype.api",
        "org.nuxeo.ecm.platform.mimetype.core",
        "org.nuxeo.ecm.platform.filemanager.api",
        "org.nuxeo.ecm.platform.filemanager.core",
        "com.leroymerlin.labs.webapp.test:OSGI-INF/core-types-contribTest.xml",
        "com.leroymerlin.labs.webapp" })
public class LabsSiteWebAppUtilsTest {

    @Inject
    private CoreSession session;

    @Test
    public void iCanGetFoldersUnderFolder() {
        String directoryPath = FileUtils.getResourcePathFromContext("onpackage/testFoldersInFolder/");
        assertThat(
                LabsSiteWebAppUtils.getFoldersUnderFolder(directoryPath).size(),
                is(3));
    }

    @Test
    public void iCanGetLatestUploadsPageProvider() throws Exception {
        DocumentModel site = createSite();

        PageClasseur classeur = new PageClasseurAdapter.Model(session,
                site.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(),
                "ma page classeur").desc("desc page classeur").create();
        assertThat(classeur.getFolders().size(), is(0));

        classeur.addFolder("My Folder", null);
        session.save();
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(), is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "title");
        session.save();
        assertThat(folder.getFiles().size(), is(1));
        session.save();

        Tools.getAdapter(LabsPublisher.class, classeur.getDocument(), null).publish();

        PageProvider<DocumentModel> latestUploadsPageProvider = LabsSiteWebAppUtils.getLatestUploadsPageProvider(
                site, 0, session);
        assertNotNull(latestUploadsPageProvider);
        List<DocumentModel> lastUploadedDoc = latestUploadsPageProvider.getCurrentPage();
        assertNotNull(lastUploadedDoc);
        assertEquals(1, lastUploadedDoc.size());
    }

    private DocumentModel createSite() throws ClientException,
            PropertyException {
        DocumentModel site = session.createDocumentModel("/", "site1",
                LabsSiteConstants.Docs.SITE.type());
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        return site;
    }

    private Blob getTestBlob() {
        String filename = "pomodoro_cheat_sheet.pdf";
        File testFile = new File(
                FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setFilename(filename);
        blob.setMimeType("application/pdf");
        return blob;

    }

}
