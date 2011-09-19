package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageClasseurAddFolderPopup;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageClasseurPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.PageClasseurPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Deploy({
    "org.nuxeo.ecm.platform.types.api",
    "org.nuxeo.ecm.platform.types.core",
    "org.nuxeo.ecm.platform.mimetype.api",
    "org.nuxeo.ecm.platform.mimetype.core",
    "org.nuxeo.ecm.platform.filemanager.api",
    "org.nuxeo.ecm.platform.filemanager.core"
})
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
@Jetty(port = 8089)
public class PageClasseurPageTest {

    private static final String FOLDER2_NAME = "folder 2";
    private static final String TEST_FILE_JPG = "testFiles/vision.jpg";
    @Inject SitesRootPage rootPage;
    
    @Test
    public void pageIsReachable() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        assertTrue(pageClasseur.isLoaded());
    }
    
    @Test
    public void pageClasseurHasSubFolder() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        assertTrue(pageClasseur.hasFolder(PageClasseurPageRepositoryInit.FOLDER1_NAME));
    }

    @Test
    public void iCanAddFolder() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        PageClasseurAddFolderPopup popup = pageClasseur.addFolder();
        popup.setTitle(FOLDER2_NAME);
        pageClasseur = popup.validate();
        assertTrue(pageClasseur.hasFolder(FOLDER2_NAME));
        assertEquals(2, pageClasseur.getFolderSections().size());
    }
    
    @Test
    public void iCanDeleteFolder2() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        assertEquals(2, pageClasseur.getFolderSections().size());
        assertTrue(pageClasseur.hasFolder(PageClasseurPageRepositoryInit.FOLDER1_NAME));
        assertTrue(pageClasseur.hasFolder(FOLDER2_NAME));
        pageClasseur = pageClasseur.deleteFolder(FOLDER2_NAME);
        assertTrue(pageClasseur.hasFolder(PageClasseurPageRepositoryInit.FOLDER1_NAME));
        assertFalse(pageClasseur.hasFolder(FOLDER2_NAME));
        assertEquals(1, pageClasseur.getFolderSections().size());
    }
    
    @Test
    public void iCanAddFile() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        pageClasseur.addFile(PageClasseurPageRepositoryInit.FOLDER1_NAME);
        pageClasseur.setFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, getTestFile(TEST_FILE_JPG));
        pageClasseur = pageClasseur.validate(PageClasseurPageRepositoryInit.FOLDER1_NAME);
//        pageClasseur.home();
        assertTrue(pageClasseur.hasFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, FileUtils.getFileName(TEST_FILE_JPG)));
        assertEquals(2, pageClasseur.getFiles(PageClasseurPageRepositoryInit.FOLDER1_NAME).size());
    }
    
    @Test
    public void iCanDeleteFile() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        pageClasseur = pageClasseur.deleteFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, FileUtils.getFileName(TEST_FILE_JPG));
        assertFalse(pageClasseur.hasFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, FileUtils.getFileName(TEST_FILE_JPG)));
        assertTrue(pageClasseur.hasFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, PageClasseurPageRepositoryInit.FILE1_NAME));
        assertEquals(1, pageClasseur.getFiles(PageClasseurPageRepositoryInit.FOLDER1_NAME).size());
        
        pageClasseur = pageClasseur.deleteFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, PageClasseurPageRepositoryInit.FILE1_NAME);
        assertFalse(pageClasseur.hasFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, FileUtils.getFileName(TEST_FILE_JPG)));
        assertFalse(pageClasseur.hasFile(PageClasseurPageRepositoryInit.FOLDER1_NAME, PageClasseurPageRepositoryInit.FILE1_NAME));
        assertEquals(0, pageClasseur.getFiles(PageClasseurPageRepositoryInit.FOLDER1_NAME).size());
    }
    
    private PageClasseurPage getPageClasseur() {
        ensureLoggedIn();
        PageClasseurPage pageClasseur = rootPage.goToPageClasseur("http://localhost:8089/labssites" + "/" + PageClasseurPageRepositoryInit.SITE_URL, PageClasseurPageRepositoryInit.PAGE_CLASSEUR_TITLE);
        return pageClasseur;
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }
    
    private static File getTestFile(String fileName) {
        return new File(FileUtils.getResourcePathFromContext(fileName));
    }
    
}
