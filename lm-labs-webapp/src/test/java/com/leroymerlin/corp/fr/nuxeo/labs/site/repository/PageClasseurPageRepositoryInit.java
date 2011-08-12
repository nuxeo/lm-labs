package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import java.io.IOException;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PageClasseurUtils;

public class PageClasseurPageRepositoryInit implements RepositoryInit {

    public static final String FILE1_NAME = "pomodoro_cheat_sheet.pdf";
    public static final String FILE1_DESCRIPTION = "Ma Description";
    public static final String FOLDER1_NAME = "folder1";
    public static final String SITE_URL = "ofm";
    public static final String SITE_TITLE = "OFM";
    public static final String PAGE_CLASSEUR_TITLE = "Page Classeur";
//    public static final String PAGECLASSEUR_NAME = "pageclasseur";

    @Override
    public void populate(CoreSession session) throws ClientException {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        if (!session.exists(new PathRef(root.getPathAsString() + "/" + SITE_TITLE))) {
            DocumentModel ofm = session.createDocumentModel(root.getPathAsString(), SITE_TITLE, LabsSiteConstants.Docs.SITE.type());
            LabsSite site = ofm.getAdapter(LabsSite.class);
            site.setURL(SITE_URL);
            site.setTitle(SITE_TITLE);
            ofm = session.createDocument(ofm);
            PageClasseur classeur = new PageClasseurAdapter.Model(session, ofm.getPathAsString() + "/" + LabsSiteConstants.Docs.TREE.docName(), PAGE_CLASSEUR_TITLE).create();
            classeur.setTitle(PAGE_CLASSEUR_TITLE);
            DocumentModel classeurDoc = classeur.getDocument();
            DocumentModel folder = session.createDocumentModel(classeurDoc.getPathAsString(), FOLDER1_NAME, "Folder");
            folder.setPropertyValue("dc:title", FOLDER1_NAME);
            folder = session.createDocument(folder);
            Blob blob = null;
            try {
                blob = new FileBlob(getClass().getResourceAsStream("/" + FILE1_NAME));
                blob.setFilename(FILE1_NAME);
                PageClasseurUtils.importBlobInPageClasseur(classeurDoc, folder.getId(), FILE1_DESCRIPTION, blob);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            session.save();
        }
    }

}
