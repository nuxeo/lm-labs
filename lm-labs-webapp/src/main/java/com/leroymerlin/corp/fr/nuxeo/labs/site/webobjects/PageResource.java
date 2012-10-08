package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.rest.DocumentHelper;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.IllegalParameterException;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.forum.LMForum;
import com.leroymerlin.corp.fr.nuxeo.forum.LMForumImpl;
import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsPageCustomView;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NoPublishException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate.LabsTemplate;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.AuthorFullName;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.SecurityDataHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {

    public static final String ELEMENTS_PER_PAGE = "elementsPerPage";

    private static final String FAILED_TO_UPDATE_PAGE_DESCRIPTION = "Failed to update page description\n";

    public static final String DC_DESCRIPTION = "dc:description";

    public static final String DC_TITLE = "dc:title";

    private static final String PROPNAME_COLLAPSETYPE = Schemas.PAGE.prefix() + ":collapseType";

    private static final String ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT = " isn't authorized to display this element!";

    public static final String COPYOF_PREFIX = "Copie de ";

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";

    //private static final String GENERATED_LESS_TEMPLATE = "resources/less/generatedLess.ftl";

    private static final Log LOG = LogFactory.getLog(PageResource.class);

    private static final String[] MESSAGES_TYPE = new String[] { "error",
            "info", "success", "warning" };

    private LabsBase labsBaseAdapter;

    public AuthorFullName afn;

    protected enum PageCreationLocation {
        TOP("top"),
        SAME("same"),
        UNDER("under");

        private String location;

        private static final Map<String, PageCreationLocation> stringToEnum = new HashMap<String, PageCreationLocation>();
        static { // Initialize map from constant name to enum constant
            for (PageCreationLocation op : values())
                stringToEnum.put(op.location(), op);
        }

        // Returns Operation for string, or null if string is invalid
        public static PageCreationLocation fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        private PageCreationLocation(String location) {
            this.location = location;
        }

        public String location() {
            return location;
        }

    }
    
    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        if (args != null && args.length > 0) {
            if (args[0] instanceof DocumentModel) {
                String userName = getContext().getPrincipal().getName();
                DocumentModel document = (DocumentModel) args[0];
                initLabsBaseAdapter(document);
                authorize(userName, document);
            }
        }
    }

    /**
     * @param userName
     * @param document
     */
    private void authorize(String userName, DocumentModel document) {
        try {
            String principalName = ctx.getPrincipal().getName();
            LabsSite site = CommonHelper.siteDoc(doc).getSite();
            if (site.isAdministrator(principalName)
                    || (site.isContributor(principalName) && !site.isSiteTemplate())) {
                return;
            }
            //TODO use facet instead
            if (!Docs.LABSNEWS.type().equals(document.getType()) &&
                    !Docs.LABSTOPIC.type().equals(document.getType())) {
                boolean authorized = labsBaseAdapter.isAuthorizedToDisplay();
                authorized = authorized && !labsBaseAdapter.isDeleted() && !site.isSiteTemplate();
                if (!authorized) {
                    throw new WebResourceNotFoundException(userName
                            + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT);
                }
            }
        } catch (ClientException e) {
            throw new WebResourceNotFoundException(userName
                    + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT, e);
        }
    }

    /**
     * @param document
     */
    private void initLabsBaseAdapter(DocumentModel document) {
        if (LabsSiteConstants.Docs.SITE.type().equals(document.getType())) {
            labsBaseAdapter = Tools.getAdapter(LabsSite.class, document, ctx.getCoreSession());
        } else {
            labsBaseAdapter = Tools.getAdapter(Page.class, doc, ctx.getCoreSession());
        }
    }

    public boolean isAuthorizedToDisplay() throws ClientException {
        if (!Docs.LABSNEWS.type().equals(doc.getType())) {
            return labsBaseAdapter.isAuthorizedToDisplay();
        }
        return true;
    }

    public boolean isAuthorizedToDisplay(DocumentModel pDocument)
            throws ClientException {
        if (pDocument != null && !Docs.LABSNEWS.type().equals(pDocument.getType())) {
            CoreSession session = ctx.getCoreSession();
            return Tools.getAdapter(Page.class, pDocument, session) != null ? Tools.getAdapter(
                Page.class, pDocument, session).isAuthorizedToDisplay() : false;
        }
        return true;
    }

    public boolean isVisible() throws ClientException {
        return labsBaseAdapter.isVisible();
    }

    public Page getPage() throws ClientException {
        if (labsBaseAdapter instanceof LabsSiteAdapter) {
            return Tools.getAdapter(Page.class, 
                    ((LabsSiteAdapter) labsBaseAdapter).getIndexDocument(), ctx.getCoreSession());
        } else {
            return (Page) labsBaseAdapter;
        }
    }
    
    public String getProperty(String prop, String defaultValue){
        return Framework.getProperty(prop, defaultValue);
    }
    
    public int getPropertyMaxSizeFileRead(){
        int result = 6;
        String property = getProperty("labs.max.size.file.read", "" + result);
        try {
            result = new Integer(property).intValue();
        } catch (NumberFormatException e) {
            WebException.wrap(e);
        }
        return result * 1048576;
    }


    @Override
    public <A> A getAdapter(Class<A> adapter) {
        return doc.getAdapter(adapter) != null ? doc.getAdapter(adapter)
                : super.getAdapter(adapter);
    }

    @GET
    @Path("displayBrowseTree")
    public Object doBrowseTree() {
        Template template = getTemplate(BROWSE_TREE_VIEW);
        return template;
    }

    @POST
    @Path("updateDescriptionCKEIP")
    public Object updateDescription(@FormParam("content") String description) {
        try {
            doc.setPropertyValue(DC_DESCRIPTION, description);
            getCoreSession().saveDocument(doc);
            getCoreSession().save();
        } catch (Exception e) {
            LOG.error(FAILED_TO_UPDATE_PAGE_DESCRIPTION + e.getMessage());
            return Response.serverError().status(Status.NOT_MODIFIED).entity(FAILED_TO_UPDATE_PAGE_DESCRIPTION).build();
        }
        return Response.status(Status.OK).build();
    }

    @POST
    @Path("@move")
    public Response doMove(@FormParam("destination") String destinationId,
            @FormParam("view") String view,
            @FormParam("redirect") String redirect) throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel destination = session.getDocument(
                new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath()
                    + viewUrl
                    + "?message_error=label.admin.page.move.destinationNotFolder");
        }
        try {
            session.move(doc.getRef(), destination.getRef(), null);
            session.save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error="
                    + e.getMessage());
        }
        return redirect(getPath() + viewUrl
                + "?message_success=label.admin.page.moved");
    }

    @POST
    @Path("@copy")
    public Response doCopy(@FormParam("destination") String destinationId,
            @FormParam("view") String view,
            @FormParam("redirect") String redirect) throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel destination = session.getDocument(
                new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath()
                    + viewUrl
                    + "?message_error=label.admin.page.copy.destinationNotFolder");
        }
        try {
            DocumentModel copy = session.copy(doc.getRef(),
                    destination.getRef(), null);
            Page page = Tools.getAdapter(Page.class, copy, session);
            page.setTitle(COPYOF_PREFIX + page.getTitle());
            session.saveDocument(page.getDocument());
            session.save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error="
                    + e.getMessage());
        }
        return redirect(getPath() + viewUrl
                + "?message_success=label.admin.page.copied");
    }

    public String getDC_TITLE(){
        return DC_TITLE;
    }

    public String getDC_DESCRIPTION(){
        return DC_DESCRIPTION;
    }

    public String getTOPIC_NOT_ALL_CONTRIBUTOR(){
        return LMForumImpl.TOPIC_NOT_ALL_CONTRIBUTOR;
    }

    @POST
    @Path("@managePage")
    public Response doManagePage() {
        try {
            String pageTitle = ctx.getForm().getString("updateTitlePage");
            if (pageTitle != null && StringUtils.isBlank(pageTitle)) {
                return redirect(getPath()
                        + "?message_error=label.parameters.page.save.fail.invalidPageTitle");
            }
            List<String> fieldsNotDisplayable = new ArrayList<String>();
            if(!DC_TITLE.equals(ctx.getForm().getString(DC_TITLE))){
                fieldsNotDisplayable.add(DC_TITLE);
            }
            if(!DC_DESCRIPTION.equals(ctx.getForm().getString(DC_DESCRIPTION))){
                fieldsNotDisplayable.add(DC_DESCRIPTION);
            }
            LMForum forum = Tools.getAdapter(LMForum.class, doc, getCoreSession());
            if (forum != null){
                if(!LMForumImpl.TOPIC_NOT_ALL_CONTRIBUTOR.equals(ctx.getForm().getString(LMForumImpl.TOPIC_NOT_ALL_CONTRIBUTOR))){
                    fieldsNotDisplayable.add(LMForumImpl.TOPIC_NOT_ALL_CONTRIBUTOR);
                    forum.manageAllContributors(false);
                }
                else{
                    forum.manageAllContributors(true);
                }
            }
            String templateName = ctx.getForm().getString("template");
            if (!BooleanUtils.toBoolean(ctx.getForm().getString("display-" + PROPNAME_COLLAPSETYPE))) {
                fieldsNotDisplayable.add(PROPNAME_COLLAPSETYPE);
            }
            Page page = Tools.getAdapter(Page.class, doc, ctx.getCoreSession());
            /* A GARDER
            page.setCollapseType(ctx.getForm().getString(PROPNAME_COLLAPSETYPE));
             */
            page.setNotDisplayableParameters(fieldsNotDisplayable);
            String elementsPerPage_str = ctx.getForm().getString(ELEMENTS_PER_PAGE);
            if (!StringUtils.isEmpty(elementsPerPage_str) && StringUtils.isNumeric(elementsPerPage_str)){
                int elementsPerPage = new Integer(elementsPerPage_str).intValue();
                if (elementsPerPage > -1){
                    page.setElementsPerPage(elementsPerPage);
                }
            }
            page.setCommentable(BooleanUtils.toBoolean(ctx.getForm().getString("commentablePage")));
            if (pageTitle != null) {
                page.setTitle(pageTitle);
            }
            boolean hiddenParam = BooleanUtils.toBoolean(ctx.getForm().getString("hiddenInLabsNavigation"));
            if (hiddenParam && !page.isHiddenInNavigation()) {
                page.hideInNavigation();
            } else if (!hiddenParam && page.isHiddenInNavigation()) {
                page.showInNavigation();
            }
            String contentView = ctx.getForm().getString("contentView");
            LabsPageCustomView customView = doc.getAdapter(LabsPageCustomView.class);
            customView.setCustomView(contentView);
            CoreSession session = getCoreSession();
            String documentTemplateName = Tools.getAdapter(LabsTemplate.class, doc, session).getDocumentTemplateName();
            if (!StringUtils.isEmpty(templateName) || (StringUtils.isEmpty(templateName) && !StringUtils.isEmpty(documentTemplateName))) {
                Tools.getAdapter(LabsTemplate.class, doc, session).setTemplateName(templateName);
            }
            session.saveDocument(doc);
            session.save();
            if("on".equalsIgnoreCase(ctx.getForm().getString("publishPage"))){
                LabsSiteWebAppUtils.publish(doc, session);
            }
            else{
                LabsSiteWebAppUtils.draft(doc);
            }
        } catch (NoPublishException e) {
            return redirect(getPath()
                    + "?message_error=label.parameters.page.publish.fail");
        } catch (ClientException e) {
            return redirect(getPath()
                    + "?message_error=label.parameters.page.save.fail");
        }
        return redirect(getPath()
                + "?message_success=label.parameters.page.save.success");
    }

    @GET
    @Path("@addContentView")
    public Template addContentView() {
        return getTemplate("views/LabsPage/manage.ftl");
    }

    public boolean isSingleNamePage(String name, DocumentRef parentRef) {
        
        return true;
    }

    @POST
    @Path("@addContent")
    public Response doAddContent() throws ClientException  {
        String name = ctx.getForm().getString("dc:title");
        String location = ctx.getForm().getString("location");
        boolean overwrite = BooleanUtils.toBoolean(ctx.getForm().getString("overwritePage"));
        return addContent(name, PageCreationLocation.fromString(location), overwrite);
    }

    @PUT
    @Path("@setHome")
    public Response setAsHomePage() {
        boolean setAsHome = false;
        try {
            CoreSession session = ctx.getCoreSession();
            LabsSite site = Tools.getAdapter(SiteDocument.class, doc, session).getSite();
            if (site.isAdministrator(ctx.getPrincipal().getName())) {
                site.setHomePageRef(doc.getId());
                session.saveDocument(site.getDocument());
                LabsSiteUtils.unblockInherits("", doc, session);
                setAsHome = true;
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        if (setAsHome) {
            return Response.noContent().build();
        } else {
            return Response.status(Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Returns a Map containing all "flash" messages
     *
     * @return
     */
    public Map<String, String> getMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        FormData form = ctx.getForm();
        for (String type : MESSAGES_TYPE) {
            String message = form.getString("message_" + type);
            if (StringUtils.isNotBlank(message)) {
                messages.put(type, message);
            }
        }
        return messages;

    }

    @Override
    public Resource initialize(WebContext ctx, ResourceType type,
            Object... args) {
        // TODO Auto-generated method stub
        return super.initialize(ctx, type, args);
    }

    @GET
    @Path("@search")
    @Override
    public Object search() {
        final HttpServletRequest request = ctx.getRequest();
        String query = request.getParameter("query");
        if (query == null) {
            String fullText = request.getParameter("fullText");
            if (fullText == null) {
                throw new IllegalParameterException("Expecting a query or a fullText parameter");
            }
            String orderBy = request.getParameter("orderBy");
            String orderClause = "";
            if (orderBy != null) {
                orderClause = " ORDER BY " + orderBy;
            }
            String path;
            if (doc.isFolder()) {
                path = doc.getPathAsString();
                //Always in labsSiste the path is the URL of site
                //The search is after the folder Tree
                path = path + "/" + LabsSiteConstants.Docs.TREE.docName();
            } else {
                path = doc.getPath().removeLastSegments(1).toString();
            }
            query = "SELECT * FROM Document WHERE (ecm:fulltext = \"" + fullText
                    + "\") AND (ecm:isCheckedInVersion = 0) AND (ecm:path STARTSWITH \"" + path + "\")" + orderClause;
        }
        try {
            DocumentModelList docs = ctx.getCoreSession().query(query);
            return getView("search").arg("query", query).arg("result", docs);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    protected Response addContent(String name, PageCreationLocation location, boolean overwrite) throws ClientException {
        if (location == null) {
            location = PageCreationLocation.TOP;
        }
        DocumentModel parent = doc;
        final CoreSession session = getCoreSession();
        try {
            LabsSite labsSite = Tools.getAdapter(SiteDocument.class, doc, session).getSite();
            if (overwrite && !labsSite.isAdministrator(ctx.getPrincipal().getName())) {
                return Response.status(Status.UNAUTHORIZED).build();
            }
            if (PageCreationLocation.SAME.equals(location)) {
                parent = session.getParentDocument(doc.getRef());
            } else if (PageCreationLocation.TOP.equals(location)) {
                parent = labsSite.getTree();
            }
            if (overwrite) {
                DocumentModel deletedPageDoc = LabsSiteUtils.getPageName(name, parent.getRef(), session);
                if (deletedPageDoc != null){
                    session.removeDocument(deletedPageDoc.getRef());
                }
            } else {
                if (LabsSiteUtils.pageNameExists(name, parent.getRef(), session)){
                    return Response.ok("existedPageName").build();
                }
            }
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        DocumentModel newDoc = DocumentHelper.createDocument(ctx, parent, name);
        newDoc.setPropertyValue("dc:title", name);
        if (Docs.HTMLPAGE.type().equals(newDoc.getType())) {
            List<String> fieldsNotDisplayable = new ArrayList<String>();
            fieldsNotDisplayable.add(PROPNAME_COLLAPSETYPE);
            Page page = Tools.getAdapter(Page.class, newDoc, ctx.getCoreSession());
            page.setNotDisplayableParameters(fieldsNotDisplayable);
        }
        session.saveDocument(newDoc);

        final DocumentModel myForumDoc = newDoc;
        if (myForumDoc.getType().equals(LabsSiteConstants.Docs.PAGEFORUM.type())) {
            UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(session){
                @SuppressWarnings("deprecation")
                @Override
                public void run() throws ClientException {
                    SecurityData data = SecurityDataHelper.buildSecurityData(myForumDoc);
                    data.addModifiablePrivilege(SecurityConstants.MEMBERS, SecurityConstants.ADD_CHILDREN, true);

                    SecurityDataHelper.updateSecurityOnDocument(myForumDoc, data);
                    session.save();
                }

            };
            runner.runUnrestricted();
        }
        return Response.ok(URIUtils.quoteURIPathComponent(ctx.getUrlPath(newDoc), false)).build();
    }

    /**
     * don't forget to complete the afn in your doGet
     * @param pAuthor
     * @return
     */
    public String getFullName(String pAuthor){
        if (afn != null){
            return afn.getFullName(pAuthor);
        }
        return "";
    }

    public long getNow(){
        return Calendar.getInstance().getTimeInMillis();
    }
    
    @PUT @Path("@hideInNavigation")
    public Response doHideInNavigation() {
        Page page = doc.getAdapter(Page.class);
        boolean hidden = false;
        try {
            page.hideInNavigation();
            getCoreSession().saveDocument(doc);
            hidden = page.isHiddenInNavigation();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        if (hidden) {
            return Response.noContent().build();
        } else {
            return Response.status(Status.NOT_MODIFIED).build();
        }
    }
    
    @PUT @Path("@showInNavigation")
    public Response doShowInNavigation() {
        Page page = doc.getAdapter(Page.class);
        boolean hidden = true;
        try {
            page.showInNavigation();
            getCoreSession().saveDocument(doc);
            hidden = page.isHiddenInNavigation();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        if (hidden) {
            return Response.status(Status.NOT_MODIFIED).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    public String getContentView() throws ClientException {
        LabsPageCustomView customView = doc.getAdapter(LabsPageCustomView.class);
        return customView.getContentView();
    }
}