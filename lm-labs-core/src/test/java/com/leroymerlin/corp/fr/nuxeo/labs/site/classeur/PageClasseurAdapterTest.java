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
import com.leroymerlin.common.core.security.PermissionsHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.LabstTest;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
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
        classeur.addFolder("My Folder", null);
        session.save();
        
        assertThat(classeur.getFolders().size(),is(1));
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getTitle(),is("My Folder"));

        classeur.removeFolder("My Folder");
        assertThat(classeur.getFolders().size(),is(0));
        
    }
    
    @Test
    public void iCanAddFolderWithQuotes() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("Folder '1'", null);
        session.save();
        
        assertThat(classeur.getFolders().size(),is(1));
        String name = classeur.getFolders().get(0).getDocument().getName();
        assertFalse(name.contains("'"));
    }
    
    
    @Test
    public void iCanAddFolderWithDoubleQuotes() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("Folder \"1\"", null);
        session.save();
        
        assertThat(classeur.getFolders().size(),is(1));
        String name = classeur.getFolders().get(0).getDocument().getName();
        assertFalse(name.contains("\""));
    }
    
    @Test
    public void iCanGetFolderByThisNameFromAPageClasseur() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder2", null);
        classeur.addFolder("My Folder", null);
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
        classeur.addFolder("My Folder2", null);
        classeur.addFolder("My Folder", null);
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
    public void iCanGetDescriptionFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder2", "description1");
        classeur.addFolder("My Folder", "description2");
        session.save();
        
        PageClasseurFolder folder = classeur.getFolder("My Folder");
        assertNotNull(folder);
        assertThat(folder.getDescription(),is("description2"));
        
        DocumentModel document = classeur.getFolder("My Folder").getDocument();
		document.setPropertyValue(AbstractLabsBase.DC_DESCRIPTION, "dede");
		session.saveDocument(document);
        session.save();
        
        
        folder = classeur.getFolder("My Folder");
        assertNotNull(folder);
        assertThat(folder.getDescription(),is("dede"));
        folder = classeur.getFolder("My Folder2");
        assertNotNull(folder);
    }
    
    @Test
    public void iCantAddTwoFolderOfSameName() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        assertThat(classeur.getFolders().size(),is(1));
        try {
            classeur.addFolder("My Folder", null);
            fail("Should not be able to create two folder of same name");
        } catch(ClasseurException e) {
        }
        
        classeur.addFolder("My Folder 2", null);
        session.save();        
        assertThat(classeur.getFolders().size(),is(2));

        
    }
    
    @Test
    public void iCanAddFilesToAFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "title");
        session.save();
        assertThat(folder.getFiles().size(),is(1));
        folder.removeFile("title");
        session.save();
        assertThat(folder.getFiles().size(),is(0));
        
        
        
    }
    
    @Test
    public void iCanGetTitleOfFile() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "title");
        session.save();
        assertThat(folder.getFiles().size(),is(1));
        assertThat(folder.getFiles().get(0).getTitle(),is("title"));
    }
    
    @Test
    public void iCanGetDefaultTitleOfFile() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", null);
        session.save();
        assertThat(folder.getFiles().size(),is(1));
        assertThat(folder.getFiles().get(0).getTitle(),is("pomodoro_cheat_sheet.pdf"));
    }
    
    @Test
    public void iCanRenameTitleOfFile() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "titlerr");
        session.save();
        folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(1));
        DocumentModel file = folder.getFiles().get(0);
        assertThat(file.getTitle(),is("titlerr"));
        file.setPropertyValue("dc:title", "title2");
        session.saveDocument(file);
        session.save();
        
        String path = classeur.getDocument().getPathAsString();
        DocumentModel doc = session.getDocument(new PathRef(path));
        classeur = Tools.getAdapter(PageClasseur.class, doc, session);
        folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(1));
        file = folder.getFiles().get(0);
        assertThat((String)file.getTitle(),is("title2"));
    }
    
    @Test
    public void iCanModifyDescriptionOfFile() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        
        classeur.addFolder("My Folder", null);
        session.save();        
        PageClasseurFolder folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(0));
        folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "titlerr");
        session.save();
        folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(1));
        DocumentModel file = folder.getFiles().get(0);
        assertThat(file.getTitle(),is("titlerr"));
        file.setPropertyValue("dc:description", "Pomodoro cheat sheet56");
        session.saveDocument(file);
        session.save();
        
        String path = classeur.getDocument().getPathAsString();
        DocumentModel doc = session.getDocument(new PathRef(path));
        classeur = Tools.getAdapter(PageClasseur.class, doc, session);
        folder = classeur.getFolders().get(0);
        assertThat(folder.getFiles().size(),is(1));
        file = folder.getFiles().get(0);
        assertThat((String)file.getPropertyValue("dc:description"),is("Pomodoro cheat sheet56"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void iCannotAddNullBlobToAFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        classeur.addFolder("My Folder", null);
        session.save();        

        PageClasseurFolder folder = classeur.getFolders().get(0);
        folder.addFile(null, "null desc", "title");
        
    }
    
    @Test
    public void iCanSetFolderAsDeleted() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        PageClasseurFolder folder = classeur.addFolder("My Folder", null);
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
        PageClasseurFolder folder = classeur.addFolder("My Folder", null);
        session.save();        
        DocumentModel file = folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "title");
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
        PageClasseurFolder folder = classeur.addFolder("My Folder", null);
        String folderId = folder.getDocument().getId();
        session.save();        
        PermissionsHelper.addPermission(classeur.getDocument(), SecurityConstants.READ_WRITE, USERNAME1 , true);
        DocumentModel file = folder.addFile(getTestBlob(), "Pomodoro cheat sheet", "title");
        session.save();
        assertEquals(1, folder.getFiles().size());
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));

        CoreSession cgmSession = changeUser(USERNAME1);
        DocumentModel cgmFolderDoc = cgmSession.getDocument(new IdRef(folderId));
        PageClasseurFolder cgmFolder = Tools.getAdapter(PageClasseurFolder.class, cgmFolderDoc, session);
        assertEquals(1, cgmFolder.getFiles().size());
        
        modified = folder.hide(file);
        assertTrue(modified);
        session.save();
        assertTrue(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());

        cgmSession.disconnect();
        cgmSession = changeUser(USERNAME1);
        cgmFolderDoc = cgmSession.getDocument(new IdRef(folderId));
        cgmFolder = Tools.getAdapter(PageClasseurFolder.class, cgmFolderDoc, cgmSession);
        assertEquals(0, cgmFolder.getFiles().size());
        
        DocumentModel folderDoc = session.getDocument(new IdRef(folderId));
        folder = Tools.getAdapter(PageClasseurFolder.class, folderDoc, session);
        modified = folder.show(file);
        assertTrue(modified);
        session.save();
        assertFalse(file.getFacets().contains(FacetNames.LABSHIDDEN));
        assertEquals(1, folder.getFiles().size());
        
        // FIXME
//        cgmSession.disconnect();
//        CoreSession cgmSession2 = changeUser(USERNAME1);
//        DocumentModel cgmFolderDoc2 = cgmSession2.getDocument(new IdRef(folderId));
//        PageClasseurFolder cgmFolder2 = Tools.getAdapter(PageClasseurFolder.class, cgmFolderDoc2, session);
//        assertEquals(1, cgmFolder2.getFiles(session).size());
	}

    private Blob getTestBlob() {
        String filename = "pomodoro_cheat_sheet.pdf";
        File testFile = new File(FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setFilename(filename);
        return blob;
        
    }@Test
    public void iCanAddDescriptionOnFolder() throws Exception {
        PageClasseur classeur = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertThat(classeur.getFolders().size(),is(0));
        classeur.addFolder("My Folder", null);
        session.save();
        
        assertThat(classeur.getFolders().size(),is(1));
    }
}
