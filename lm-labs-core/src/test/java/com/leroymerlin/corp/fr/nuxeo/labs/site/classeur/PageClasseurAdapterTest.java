package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import static org.hamcrest.CoreMatchers.*;
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
public class PageClasseurAdapterTest {
    private static final String TITLE2 = "page_classeur2";
    private static final String DESCR3 = "Ma descr 3";
    private static final String TITLE3 = "Page Classeur 3";
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

    private Blob getTestBlob() {
        String filename = "pomodoro_cheat_sheet.pdf";
        File testFile = new File(FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setFilename(filename);
        return blob;
        
    }
}
