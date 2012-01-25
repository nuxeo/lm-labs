package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.LabstTest;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PermissionsHelper;
@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy({
    "org.nuxeo.ecm.platform.types.api",
    "org.nuxeo.ecm.platform.types.core",
    "org.nuxeo.ecm.platform.mimetype.api",
    "org.nuxeo.ecm.platform.mimetype.core",
    "org.nuxeo.ecm.platform.filemanager.api",
    "org.nuxeo.ecm.platform.filemanager.core"
})

@RepositoryConfig(init=PageClasseurRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageClasseurAdapterTest extends LabstTest {
    private static final String TITLE2 = "page_classeur2";
    private static final String DESCR3 = "Ma descr 3";
    private static final String TITLE3 = "Page Classeur 3";
    private static final String USERNAME1 = "CGM";

    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageListe() throws Exception {
        assertTrue(session.exists(new PathRef("/page_classeur")));
    }
    
    @Test
    public void iCanCreateDocumentUsingAdapter() throws Exception {
        PageClasseur adapter = new PageClasseurAdapter.Model(session, "/", TITLE2).create();
        assertNotNull(adapter);
        DocumentModel doc = adapter.getDocument();
        assertNotNull(doc);
        assertTrue(session.exists(doc.getRef()));
        assertEquals(TITLE2, adapter.getTitle());
    }
    
    @Test
    public void iCanCreateDocumentUsingAdapterAndSetDescription() throws Exception {
        PageClasseur adapter = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertNotNull(adapter);
        DocumentModel doc = adapter.getDocument();
        assertNotNull(doc);
        assertTrue(session.exists(doc.getRef()));
        assertEquals(TITLE3, adapter.getTitle());
        assertEquals(DESCR3, adapter.getDescription());
    }
    
    @Test
    public void iCanGetFoldersFromAPageClasseur() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder");
        session.save();
        
        assertThat(classeur.getFolders().size(),is(1));
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getTitle(),is("My Folder"));

        classeur.removeFolder("My Folder");
        assertThat(classeur.getFolders().size(),is(0));
        
    }
    
    @Test
    public void iCanGetFolderByThisNameFromAPageClasseur() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder2");
        classeur.addFolder("My Folder");
        session.save();
        
        PageClasseurFolder folder = classeur.getFolder("My Folder");
        assertNotNull(folder);
        assertThat(folder.getTitle(),is("My Folder"));

        classeur.removeFolder("My Folder");
        assertNull(classeur.getFolder("My Folder"));
    }
    
    @Test
    public void iCanGetRemaneFolderByThisNameFromAPageClasseur() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder2");
        classeur.addFolder("My Folder");
        session.save();
        
        classeur.renameFolder(classeur.getFolder("My Folder2").getDocument().getRef().toString(), "My Folder3");
        session.save();
        
        PageClasseurFolder folder = classeur.getFolder("My Folder3");
        assertNotNull(folder);
        assertThat(folder.getTitle(),is("My Folder3"));
        folder = classeur.getFolder("My Folder2");
        assertNull(folder);
    }
    
    @Test
    public void iCantAddTwoFolderOfSameName() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder");
        session.save();        
        assertThat(classeur.getFolders().size(),is(1));
        try {
            classeur.addFolder("My Folder");
            fail("Should not be able to create two folder of same name");
        } catch(ClasseurException e) {
        }
        
        classeur.addFolder("My Folder 2");
        session.save();        
        assertThat(classeur.getFolders().size(),is(2));

        
    }
    
    @Test
    public void iCanAddFilesToAFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder");
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet");
        session.save();
        assertThat(folder.getFiles().size(),is(1));
        folder.removeFile("pomodoro_cheat_sheet.pdf");
        session.save();
        assertThat(folder.getFiles().size(),is(0));
        
        
        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void iCannotAddNullBlobToAFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        classeur.addFolder("My Folder");
        session.save();        

        PageClasseurFolder folder = classeur.getFolders().get(0);
        folder.addFile(null, "null desc");
        
    }
    
    @Test
    public void iCanSetFolderAsDeleted() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        PageClasseurFolder folder = classeur.addFolder("My Folder");
        session.save();
        assertFalse(classeur.getFolders().isEmpty());
        boolean deleted = folder.setAsDeleted();
        session.save();
        assertTrue(deleted);
        assertEquals(LifeCycleConstants.DELETED_STATE, folder.getDocument().getCurrentLifeCycleState());
        assertTrue(classeur.getFolders().isEmpty());
    }
    
    @Test
	public void iCanHideShowFile() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        PageClasseurFolder folder = classeur.addFolder("My Folder");
        session.save();        
        DocumentModel file = folder.addFile(getTestBlob(), "Pomodoro cheat sheet");
        session.save();
        assertEquals(1, folder.getFiles().size());
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));
        
        folder.hide(file);
        session.save();
        assertTrue(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());

        folder.show(file);
        session.save();
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());
	}
    
    @Test
	public void iCanHideShowFileForContributors() throws Exception {
    	boolean modified = false;
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        PageClasseurFolder folder = classeur.addFolder("My Folder");
        String folderId = folder.getDocument().getId();
        session.save();        
        PermissionsHelper.addPermission(classeur.getDocument(), SecurityConstants.READ_WRITE, USERNAME1 , true);
        DocumentModel file = folder.addFile(getTestBlob(), "Pomodoro cheat sheet");
        session.save();
        assertEquals(1, folder.getFiles().size());
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));

        CoreSession cgmSession = changeUser(USERNAME1);
        DocumentModel cgmFolderDoc = cgmSession.getDocument(new IdRef(folderId));
        PageClasseurFolder cgmFolder = cgmFolderDoc.getAdapter(PageClasseurFolder.class);
        assertEquals(1, cgmFolder.getFiles().size());
        
        modified = folder.hide(file);
        assertTrue(modified);
        session.save();
        assertTrue(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());

        cgmSession.disconnect();
        cgmSession = changeUser(USERNAME1);
        cgmFolderDoc = cgmSession.getDocument(new IdRef(folderId));
        cgmFolder = cgmFolderDoc.getAdapter(PageClasseurFolder.class);
        assertEquals(0, cgmFolder.getFiles().size());
        
        DocumentModel folderDoc = session.getDocument(new IdRef(folderId));
        folder = folderDoc.getAdapter(PageClasseurFolder.class);
        modified = folder.show(file);
        assertTrue(modified);
        session.save();
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());
        
        // FIXME
//        cgmSession.disconnect();
//        CoreSession cgmSession2 = changeUser(USERNAME1);
//        DocumentModel cgmFolderDoc2 = cgmSession2.getDocument(new IdRef(folderId));
//        PageClasseurFolder cgmFolder2 = cgmFolderDoc2.getAdapter(PageClasseurFolder.class);
//        assertEquals(1, cgmFolder2.getFiles().size());
	}

    private Blob getTestBlob() {
        String filename = "pomodoro_cheat_sheet.pdf";
        File testFile = new File(FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setFilename(filename);
        return blob;
        
    }
}
