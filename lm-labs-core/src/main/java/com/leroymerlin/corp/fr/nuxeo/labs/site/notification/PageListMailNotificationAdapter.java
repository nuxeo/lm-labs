package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.query.sql.NXQL;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageListMailNotificationAdapter extends SubDocumentsMailNotificationAdapter {

    public PageListMailNotificationAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<DocumentModel> getSubDocumentsToNotify() throws ClientException {
        if (subDocuments == null) {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM Document WHERE ")
            .append(NXQL.ECM_PARENTID).append(" = '").append(doc.getId()).append("'")
            .append(" AND ")
            .append(NXQL.ECM_ISPROXY).append(" = 0")
            .append(" AND ")
            .append(NXQL.ECM_ISVERSION).append(" = 0")
            .append(" AND ")
            .append(NXQL.ECM_LIFECYCLESTATE).append(" <> 'deleted'")
            .append(" AND ")
            .append(NXQL.ECM_PRIMARYTYPE).append(" = '").append(Docs.PAGELIST_LINE.type()).append("'")
            .append(" AND ")
            .append(MailNotification.PROPERTY_TONOTIFY).append(" = 1");
            subDocuments = doc.getCoreSession().query(query.toString());
        }
        return subDocuments;
    }

}
