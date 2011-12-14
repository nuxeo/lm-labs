package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs.PAGECLASSEUR;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.query.sql.NXQL;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurAdapter extends AbstractPage implements PageClasseur {

    public PageClasseurAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public static class Model {
        private final DocumentModel doc;

        /**
         * PageClasseur adapter = new PageClasseurAdapter.Model(session, "/",
         * "titre").desc("description").create();
         *
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title)
                throws ClientException {
            this.doc = session.createDocumentModel(parentPath, title,
                    PAGECLASSEUR.type());
            this.doc.setPropertyValue("dc:title", title);
        }

        /**
         * Sets dc:description.
         *
         * @param description
         * @return
         * @throws PropertyException
         * @throws ClientException
         */
        public Model desc(String description) throws PropertyException,
                ClientException {
            PageClasseurAdapter.setDescription(this.doc, description);
            return this;
        }

        /**
         * Creates document model in repository.
         *
         * @return an adapter
         * @throws ClientException
         */
        public PageClasseur create() throws ClientException {
            DocumentModel doc = this.doc.getCoreSession()
                    .createDocument(this.doc);
            return doc.getAdapter(PageClasseur.class);
        }
    }

    @Override
    public DocumentModel getDocument() {
        return this.doc;
    }

    @Override
    public List<PageClasseurFolder> getFolders() {
        List<PageClasseurFolder> result = new ArrayList<PageClasseurFolder>();
        try {
            StringBuilder sb = new StringBuilder("SELECT * From Document");
            sb.append(" WHERE ").append(NXQL.ECM_PARENTID).append(" = '").append(doc.getId()).append("'")
            .append(" AND ").append(NXQL.ECM_PRIMARYTYPE).append(" = '").append(Docs.PAGECLASSEURFOLDER.type()).append("'")
            .append(" AND ").append(NXQL.ECM_ISPROXY).append(" = 0")
            .append(" AND ").append(NXQL.ECM_ISVERSION).append(" = 0")
            .append(" AND ").append(NXQL.ECM_LIFECYCLESTATE).append(" <> '").append(LifeCycleConstants.DELETED_STATE).append("'");
            sb.append(" ORDER BY dc:title ASC");
            DocumentModelList list = doc.getCoreSession()
                    .query(sb.toString());
            for (DocumentModel child : list) {
                result.add(new PageClasseurFolderImpl(child));
            }
            return result;
        } catch (ClientException e) {
            return result;
        }
    }

    @Override
    public PageClasseurFolder addFolder(String title) throws ClientException {
        if (folderAlreadyExists(title)) {
            throw new ClasseurException(
                    "label.classeur.error.cant_add_two_folder_of_same_name");
        }

        if (!StringUtils.isEmpty(title)) {
            CoreSession session = doc.getCoreSession();
            DocumentModel folder = session.createDocumentModel(
                    doc.getPathAsString(), title, "Folder");
            folder.setPropertyValue("dc:title", title);
            folder = session.createDocument(folder);
            return new PageClasseurFolderImpl(folder);
        }
        return null;
    }

    private boolean folderAlreadyExists(String title) throws ClientException {
        return getFolder(title) != null;
    }

    @Override
    public PageClasseurFolder getFolder(String title) throws ClientException {
        for (PageClasseurFolder folder : getFolders()) {
            if (title.equals(folder.getTitle())) {
                return folder;
            }
        }
        return null;
    }

    @Override
    public void removeFolder(String title) throws ClientException {
        PageClasseurFolder folder = getFolder(title);
        if (folder != null) {
            doc.getCoreSession().removeDocument(folder.getDocument().getRef());
        }

    }

    @Override
    public void renameFolder(String idRef, String newName) throws ClientException {
        CoreSession session = doc.getCoreSession();
        DocumentModel document = session.getDocument(new IdRef(idRef));
        document.setPropertyValue("dc:title", newName);
        session.saveDocument(document);
    }



}
