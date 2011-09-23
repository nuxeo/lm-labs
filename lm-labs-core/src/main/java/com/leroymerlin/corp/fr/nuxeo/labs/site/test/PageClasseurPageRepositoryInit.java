package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageClasseurPageRepositoryInit extends OfmRepositoryInit {

    public static final String FILE1_NAME = "pomodoro_cheat_sheet.pdf";
    public static final String FILE1_DESCRIPTION = "Ma Description";
    public static final String FOLDER1_NAME = "folder1";
    public static final String PAGE_CLASSEUR_TITLE = "Page Classeur";

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);
        PageClasseur classeur = new PageClasseurAdapter.Model(session,
                ofm.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(),
                PAGE_CLASSEUR_TITLE).create();
        classeur.setTitle(PAGE_CLASSEUR_TITLE);

        PageClasseurFolder folder = classeur.addFolder(FOLDER1_NAME);
        session.save();
        try {
            Blob blob = new FileBlob(getClass().getResourceAsStream(
                    "/" + FILE1_NAME));
            blob.setFilename(FILE1_NAME);
            DocumentModel addFile = folder.addFile(blob, FILE1_DESCRIPTION);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        session.save();
    }

}