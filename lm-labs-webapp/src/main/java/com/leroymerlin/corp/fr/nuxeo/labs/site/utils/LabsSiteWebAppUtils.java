package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.providers.LatestUploadsPageProvider;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public final class LabsSiteWebAppUtils {

    private static final String LATEST_UPLOADS_PAGEPROVIDER = "latest_uploads";

    private LabsSiteWebAppUtils() {
    }


    public static boolean canGetPreview(final DocumentModel doc)
            throws ClientException {
        DocumentModel parent = doc.getCoreSession().getParentDocument(
                doc.getRef());
        return Docs.FOLDER.type().equals(parent.getType())
                && parent.getCoreSession().getDocument(parent.getParentRef()).hasSchema(
                        Schemas.PAGE.getName())
                && doc.getAdapter(BlobHolder.class) != null;
    }


    public static PageProvider<DocumentModel> getLatestUploadsPageProvider(
            DocumentModel doc, long pageSize) throws Exception {
        PageProviderService ppService = Framework.getService(PageProviderService.class);
        List<SortInfo> sortInfos = null;
        Map<String, Serializable> props = new HashMap<String, Serializable>();

        SiteDocument sd = doc.getAdapter(SiteDocument.class);

        props.put(
                LatestUploadsPageProvider.PARENT_DOCUMENT_PROPERTY,
                (Serializable) sd.getSite().getTree());
        @SuppressWarnings("unchecked")
        PageProvider<DocumentModel> pp = (PageProvider<DocumentModel>) ppService.getPageProvider(
                LATEST_UPLOADS_PAGEPROVIDER, sortInfos, new Long(pageSize),
                null, props, "");
        return pp;
    }



}
