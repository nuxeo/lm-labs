package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.rest.DocumentHelper;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate.LabsTemplate;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {

    private static final String FAILED_TO_UPDATE_PAGE_DESCRIPTION = "Failed to update page description\n";

    public static final String DC_DESCRIPTION = "dc:description";

    public static final String DC_TITLE = "dc:title";

    private static final String ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT = " isn't authorized to display this element!";

    public static final String COPYOF_PREFIX = "Copie de ";

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";

    private static final String GENERATED_LESS_TEMPLATE = "resources/less/generatedLess.ftl";

     private static final Log LOG = LogFactory.getLog(PageResource.class);

    private static final String[] MESSAGES_TYPE = new String[] { "error",
            "info", "success", "warning" };

    private LabsBase labsBaseAdapter;

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
            if (!Docs.LABSNEWS.type().equals(document.getType())) {
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
            labsBaseAdapter = document.getAdapter(LabsSite.class);
        } else {
            labsBaseAdapter = document.getAdapter(Page.class);
        }
    }

    public boolean isAuthorizedToDisplay() throws ClientException {
        return labsBaseAdapter.isAuthorizedToDisplay();
    }

    public boolean isAuthorizedToDisplay(DocumentModel pDocument)
            throws ClientException {
        return pDocument.getAdapter(Page.class) != null ? pDocument.getAdapter(
                Page.class).isAuthorizedToDisplay() : false;
    }

    public boolean isVisible() throws ClientException {
        return labsBaseAdapter.isVisible();
    }

    public Page getPage() throws ClientException {
        if (labsBaseAdapter instanceof LabsSiteAdapter) {
            return ((LabsSiteAdapter) labsBaseAdapter).getIndexDocument().getAdapter(
                    Page.class);
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

    @GET
    @Path(value = "generated.less")
    public Object getGeneratedLess() {
        String themeName = "";
        String style = "";
        String styleProperties = "";
        try {
            LabsSite site = doc.getAdapter(SiteDocument.class).getSite();
            SiteTheme theme = site.getThemeManager().getTheme();
            themeName = theme.getName();
            style = theme.getStyle();
            styleProperties = generateLessWithProperties(theme.getProperties());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return getTemplate(GENERATED_LESS_TEMPLATE).arg("themeName", themeName).arg(
                "addedStyle", style).arg("styleProperties", styleProperties);
    }
    
    private String generateLessWithProperties(Map<String, ThemeProperty> properties){
        StringBuilder less = new StringBuilder();
        for (ThemeProperty prop: properties.values()){
            if (StringUtils.isNotBlank(prop.getValue())){
                if(LabsSiteConstants.PropertyType.IMAGE.getType().equals(prop.getType())){
                    String pathImg = this.getContext().getBaseURL().substring(7) + prop.getValue();
                    less.append(prop.getKey() + ": \"" + pathImg + "\";\n");
                    less.append(prop.getKey() + "Relative: false;\n");
                }
                else{
                    less.append(prop.getKey() + ":" + prop.getValue() + ";\n");
                }
            }
        }
        return less.toString();
    }

    @GET
    @Path(value = "generatedAdmin.less")
    public Object getGeneratedAdminLess() {
        String themeName = "";
        try {
            LabsSite site = doc.getAdapter(SiteDocument.class).getSite();
            themeName = site.getThemeManager().getTheme().getName();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return getTemplate(GENERATED_LESS_TEMPLATE).arg("themeName", themeName).arg(
                "addedStyle", null);
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
        DocumentModel destination = doc.getCoreSession().getDocument(
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
            doc.getCoreSession().move(doc.getRef(), destination.getRef(), null);
            doc.getCoreSession().save();
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
        DocumentModel destination = doc.getCoreSession().getDocument(
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
            DocumentModel copy = doc.getCoreSession().copy(doc.getRef(),
                    destination.getRef(), null);
            Page page = copy.getAdapter(Page.class);
            page.setTitle(COPYOF_PREFIX + page.getTitle());
            doc.getCoreSession().saveDocument(page.getDocument());
            doc.getCoreSession().save();
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

    @POST
    @Path("@managePage")
    public Response doManagePage() {
        try {
            String pageTitle = ctx.getForm().getString("updateTitlePage");
            if (StringUtils.isBlank(pageTitle)) {
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

            boolean isCheckedCommentable = "on".equalsIgnoreCase(ctx.getForm().getString(
            "commentablePage"));
            String templateName = ctx.getForm().getString("template");
            Page page = doc.getAdapter(Page.class);
            page.setCommentable(isCheckedCommentable);
            page.setNotDisplayableParameters(fieldsNotDisplayable);
            page.setTitle(pageTitle);
            doc.getAdapter(LabsTemplate.class).setTemplateName(templateName);
            CoreSession session = getCoreSession();
            session.saveDocument(doc);
            session.save();
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
    public Response doAddContent() {
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
            LabsSite site = doc.getAdapter(SiteDocument.class).getSite();
            if (site.isAdministrator(ctx.getPrincipal().getName())) {
                site.setHomePageRef(doc.getId());
                doc.getCoreSession().saveDocument(site.getDocument());
                setAsHome = true;
            }
        } catch (ClientException e) {
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

    protected Response addContent(String name, PageCreationLocation location, boolean overwrite) {
        if (location == null) {
            location = PageCreationLocation.TOP;
        }
        DocumentModel parent = doc;
        CoreSession session = getCoreSession();
        try {
            LabsSite labsSite = doc.getAdapter(SiteDocument.class).getSite();
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
        return Response.ok(URIUtils.quoteURIPathComponent(ctx.getUrlPath(newDoc), false)).build();
    }

}
