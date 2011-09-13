package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public final class LabsSiteUtils {

    // Log
    private static final Log LOG = LogFactory.getLog(LabsSiteUtils.class);

    private static final DateFormat dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy hh:ss");

    private static UserManager userManager = null;

    static {
        try {
            userManager = Framework.getService(UserManager.class);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    private LabsSiteUtils() {
    }

    @Deprecated
    public static final DocumentModelList getRootFolder(
            final DocumentModel site, final CoreSession session)
            throws ClientException {
        PathRef welcomeRef = new PathRef(site.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName());
        if (site == null || session == null || !session.exists(welcomeRef)) {
            return null;
        }

        return session.getChildren(welcomeRef);
    }

    public static DocumentModel getSitesRoot(final CoreSession session)
            throws ClientException {
        return session.getDocument(new PathRef(getSitesRootPath()));

    }

    public static String getSitesRootPath() {
        return "/" + Docs.DEFAULT_DOMAIN.docName() 
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
        return parentDoc.getCoreSession().query(
                "SELECT * FROM Page WHERE ecm:parentId = '"
                        + getSiteTree(getParentSite(parentDoc)).getId()
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

    public static DocumentModel getParentSite(DocumentModel doc)
            throws ClientException {
        CoreSession session = doc.getCoreSession();
        if (Docs.SITE.type().equals(doc.getType())) {
            return doc;
        }
        while (!doc.getParentRef().equals(session.getRootDocument().getRef())) {
            doc = session.getDocument(doc.getParentRef());
            if (Docs.SITE.type().equals(doc.getType())) {
                return doc;
            }
        }
        throw new IllegalArgumentException("document '" + doc.getPathAsString()
                + "' is not lacated in a site.");
    }

    /**
     * @param document search starting point's {@link DocumentModel}
     * @return the closest parent DocumentModel having schema "page" otherwise
     *         <code>document</code>.
     */
    public static DocumentModel getClosestPage(DocumentModel document) {
        DocumentModel parentDocument = document;
        while (!LabsSiteConstants.Docs.DEFAULT_DOMAIN.type().equals(
                parentDocument.getType())) {
            try {
                parentDocument = document.getCoreSession().getParentDocument(
                        parentDocument.getRef());
                if (parentDocument.hasSchema(Schemas.PAGE.getName())) {
                    return parentDocument;
                }
            } catch (ClientException e) {
                LOG.error(e, e);
                break;
            }
        }
        return document;
    }

    public static String getCreatorUsername(DocumentModel doc)
            throws ClientException {
        return getFormattedUsername(getCreator(doc));
    }

    public static String getLastModifierUsername(DocumentModel doc)
            throws ClientException {
        return getFormattedUsername(getLastModifier(doc));
    }

    private static String getCreator(final DocumentModel doc)
            throws ClientException {
        return doc.getPropertyValue("dc:creator") != null ? (String) doc.getPropertyValue("dc:creator")
                : "";
    }

    private static String getLastModifier(DocumentModel doc)
            throws ClientException {
        return doc.getPropertyValue("dc:lastContributor") != null ? (String) doc.getPropertyValue("dc:lastContributor")
                : "";
    }

    private static String getFormattedUsername(String creator)
            throws ClientException {

        DocumentModel principal = userManager.getUserModel(creator);
        if (principal != null) {
            String lastName = (String) principal.getProperty(
                    userManager.getUserSchemaName(), "lastName");
            String firstName = (String) principal.getProperty(
                    userManager.getUserSchemaName(), "firstName");

            if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
                return creator;
            }

            return (StringUtils.isNotBlank(firstName) ? firstName : "") + " "
                    + (StringUtils.isNotBlank(lastName) ? lastName : "");
        }

        return creator;
    }

    public static String getCreated(DocumentModel doc) throws ClientException {
        GregorianCalendar date = (GregorianCalendar) doc.getPropertyValue("dc:created");
        return getFormattedDate(date.getTime());
    }

    public static String getLastModified(DocumentModel doc)
            throws ClientException {
        GregorianCalendar date = (GregorianCalendar) doc.getPropertyValue("dc:modified");
        return getFormattedDate(date.getTime());
    }

    private static String getFormattedDate(Date date) {
        return dateFormat.format(date);
    }

}
