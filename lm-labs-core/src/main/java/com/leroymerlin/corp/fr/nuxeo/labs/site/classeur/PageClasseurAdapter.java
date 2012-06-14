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
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentService;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class PageClasseurAdapter extends AbstractPage implements PageClasseur {

    public PageClasseurAdapter(DocumentModel doc) {
        super(doc);
    }

    public static class Model {
        private final DocumentModel doc;
        
        private CoreSession session;

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
        	this.session = session;
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
            DocumentModel doc = this.session.createDocument(this.doc);
            PageClasseur adapter = doc.getAdapter(PageClasseur.class);
            adapter.setSession(session);
			return adapter;
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
            sb.append(" ORDER BY ").append(NXQL.ECM_POS);
            CoreSession session = getSession();
			DocumentModelList list = session.query(sb.toString());
            for (DocumentModel child : list) {
                //result.add(new PageClasseurFolderImpl(child));
            	result.add(Tools.getAdapter(PageClasseurFolder.class, child, session));
            }
            return result;
        } catch (ClientException e) {
            return result;
        }
    }

    @Override
    public PageClasseurFolder addFolder(String title, String description) throws ClientException {
        PathSegmentService pss;
        CoreSession session = getSession();
        try {
            pss = Framework.getService(PathSegmentService.class);
        } catch (Exception e) {
            throw new ClasseurException("Unable to get PathSegmentService.");
        }
        if (folderAlreadyExists(title)) {
            throw new ClasseurException(
                    "label.classeur.error.cant_add_two_folder_of_same_name");
        }

        if (!StringUtils.isEmpty(title)) {
            DocumentModel folder = session.createDocumentModel(
//                    doc.getPathAsString(), title, 
                    LabsSiteConstants.Docs.PAGECLASSEURFOLDER.type());
            folder.setPropertyValue("dc:title", title);
            folder.setPropertyValue("dc:description", description);
            String folderName = pss.generatePathSegment(folder);
            folder.setPathInfo(doc.getPathAsString(), folderName);
            folder = session.createDocument(folder);
            return Tools.getAdapter(PageClasseurFolder.class, folder, session);
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
            getSession().removeDocument(folder.getDocument().getRef());
        }

    }

    @Override
    public void renameFolder(String idRef, String newName) throws ClientException {
    	CoreSession session = getSession();
        DocumentModel document = session.getDocument(new IdRef(idRef));
        document.setPropertyValue("dc:title", newName);
        session.saveDocument(document);
    }



}
