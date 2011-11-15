package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.core.schema.FacetNames;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurMailNotificationAdapter extends SubDocumentsMailNotificationAdapter {

    public PageClasseurMailNotificationAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<DocumentModel> getSubDocumentsToNotify() throws ClientException {
        if (subDocuments == null) {
            subDocuments = new ArrayList<DocumentModel>();
            DocumentModelList folders = doc.getCoreSession().getChildren(doc.getRef(), Docs.FOLDER.type());
            for (DocumentModel folder : folders) {
                if (folder.getAdapter(MailNotification.class).isToBeNotified()) {
                    subDocuments.add(folder);
                }
                StringBuilder query = new StringBuilder();
                query.append("SELECT * FROM Document WHERE ")
                .append(NXQL.ECM_PARENTID).append(" = '").append(folder.getId()).append("'")
                .append(" AND ")
                .append(NXQL.ECM_ISPROXY).append(" = 0")
                .append(" AND ")
                .append(NXQL.ECM_ISVERSION).append(" = 0")
                .append(" AND ")
                .append(NXQL.ECM_LIFECYCLESTATE).append(" <> 'deleted'")
                .append(" AND ")
                .append(NXQL.ECM_MIXINTYPE).append(" <> '").append(FacetNames.FOLDERISH).append("'")
                .append(" AND ")
                .append(MailNotification.PROPERTY_TONOTIFY).append(" = 1");
                DocumentModelList files = doc.getCoreSession().query(query.toString());
                if (!files.isEmpty()) {
                    subDocuments.addAll(files);
                }
            }
        }
        return subDocuments;
    }

}
