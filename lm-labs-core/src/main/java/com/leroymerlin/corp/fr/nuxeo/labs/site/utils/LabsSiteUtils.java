package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public final class LabsSiteUtils {

    // Log
    private static final Log LOG = LogFactory.getLog(LabsSiteUtils.class);


    private LabsSiteUtils() {
    }

    public static DocumentModel getSitesRoot(final CoreSession session)
            throws ClientException {
        return session.getDocument(new PathRef(getSitesRootPath()));

    }

    public static String getSitesRootPath() {
        return "/" + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName();
    }

    public static String getSiteTreePath(DocumentModel siteDoc)
            throws ClientException {
        if (!Docs.SITE.type().equals(siteDoc.getType())) {
            throw new IllegalArgumentException("document is not a "
                    + Docs.SITE.type());
        }
        return getSitesRootPath() + "/" + siteDoc.getName() + "/"
                + Docs.TREE.docName();
    }

    public static DocumentModel getSiteTree(DocumentModel siteDoc)
            throws ClientException {
        return siteDoc.getCoreSession().getDocument(
                new PathRef(getSiteTreePath(siteDoc)));
    }

    public static DocumentModelList getTreeChildren(final DocumentModel parentDoc)
            throws ClientException {

        SiteDocument sd = parentDoc.getAdapter(SiteDocument.class);
        return parentDoc.getCoreSession().query(
                "SELECT * FROM Page WHERE ecm:parentId = '"
                        + getSiteTree(sd.getSite().getDocument()).getId()
                        + "'"
                        + " AND ecm:isCheckedInVersion = 0 AND ecm:currentLifeCycleState != 'deleted'");
    }

    /**
     * @param site
     * @return JSON representation of site's tree.
     * @throws ClientException if unable to retrieve children of site.
     * @throws IllegalArgumentException if site is <code>null</code>.
     */
    public static Object getSiteMap(final DocumentModel site)
            throws ClientException, IllegalArgumentException {
        if (site == null) {
            throw new IllegalArgumentException(
                    "site's document model can not be null.");
        }
        return site.getCoreSession().getChildren(site.getRef());
    }

    /**
     * @param parent
     * @return list of Page Document models.
     * @throws ClientException if unable to retrieve Page document models under
     *             <code>parent</code>.
     * @throws IllegalArgumentException if parent is null.
     */
    public static DocumentModelList getAllDoc(final DocumentModel parent)
            throws ClientException, IllegalArgumentException {
        if (parent == null) {
            throw new IllegalArgumentException(
                    "document model can not be null.");
        }

        return parent.getCoreSession().query(
                "SELECT * FROM Page where ecm:path STARTSWITH '"
                        + parent.getPathAsString() + "'");
    }







}
