package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@Deploy({
    "org.nuxeo.ecm.platform.types.api",
    "org.nuxeo.ecm.platform.types.core",
    "org.nuxeo.ecm.platform.mimetype.api",
    "org.nuxeo.ecm.platform.mimetype.core",
    "org.nuxeo.ecm.platform.filemanager.api",
    "org.nuxeo.ecm.platform.filemanager.core"
})
@RepositoryConfig(init=PageClasseurRepositoryInit.class, user = "Administrator")
public class PageClasseurUtilsTest {

    private static final String DESCRIPTION = "Ma Description";

    private static final String FOLDER_NAME = "folder";

    @Inject
    private CoreSession session;
    
//    @Inject FileManager fileManagerService;
//    @Inject MimetypeRegistryService mimetypeRegistryService;
//    @Inject TypeService typeService;

    @Inject
    protected FeaturesRunner featuresRunner;

    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void iCanCreateFolder() throws Exception {
        DocumentModel classeur = session.getDocument(new PathRef(PageClasseurRepositoryInit.PAGECLASSEUR_CONTAINER_PATH + PageClasseurRepositoryInit.PAGECLASSEUR_NAME));
        DocumentModel folder = session.createDocumentModel(classeur.getPathAsString(), FOLDER_NAME, "Folder");
        folder = session.createDocument(folder);
    }
    
    @Test
    public void iCanAddFile() throws Exception {
        DocumentModel classeur = session.getDocument(new PathRef(PageClasseurRepositoryInit.PAGECLASSEUR_CONTAINER_PATH + PageClasseurRepositoryInit.PAGECLASSEUR_NAME));
        DocumentModel folder = session.getDocument(new PathRef(classeur.getPathAsString() + "/" + FOLDER_NAME));
        assertNotNull(folder);
        Blob blob = null;
        String filename = "pomodoro_cheat_sheet.pdf";
        File testFile = new File(FileUtils.getResourcePathFromContext("testFiles/" + filename));
        blob = new FileBlob(testFile);
        blob.setFilename(filename);
        DocumentModel doc = PageClasseurUtils.importBlobInPageClasseur(classeur, folder.getId(), DESCRIPTION, blob);
        assertNotNull(doc);
        assertEquals(filename, (String) doc.getPropertyValue("dc:title"));
        assertEquals(DESCRIPTION, (String) doc.getPropertyValue("dc:description"));
    }
    
    @Test
    public void iCannotAddNullBlob() throws Exception {
        DocumentModel classeur = session.getDocument(new PathRef(PageClasseurRepositoryInit.PAGECLASSEUR_CONTAINER_PATH + PageClasseurRepositoryInit.PAGECLASSEUR_NAME));
        DocumentModel folder = session.getDocument(new PathRef(classeur.getPathAsString() + "/" + FOLDER_NAME));
        assertNotNull(folder);
        Blob blob = null;
        thrown.expect(IllegalArgumentException.class);
        PageClasseurUtils.importBlobInPageClasseur(classeur, folder.getId(), DESCRIPTION, blob);
    }
    
    @Test
    public void iCannotAddFileToInvalidFolder() throws Exception {
        DocumentModel classeur = session.getDocument(new PathRef(PageClasseurRepositoryInit.PAGECLASSEUR_CONTAINER_PATH + PageClasseurRepositoryInit.PAGECLASSEUR_NAME));
        DocumentModel folder = session.getDocument(new PathRef(classeur.getPathAsString() + "/"));
        assertNotNull(folder);
        Blob blob = null;
        thrown.expect(IllegalArgumentException.class);
        PageClasseurUtils.importBlobInPageClasseur(classeur, folder.getId(), DESCRIPTION, blob);
    }
}
