package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
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

import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.portal.usermanager.LMNuxeoPrincipal;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {

    private static final String ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT = " isn't authorized to display this element!";
    public static final String COPYOF_PREFIX = "Copie de ";

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";
    
    private static final String GENERATED_LESS_TEMPLATE = "resources/less/generatedLess.ftl";

//    private static final Log LOG = LogFactory.getLog(PageResource.class);

    private static final String[] MESSAGES_TYPE = new String[] { "error",
            "info", "success", "warning" };
    
    private LabsBase labsBaseAdapter;


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
            if (CommonHelper.siteDoc(doc).getSite().isAdministrator(ctx.getPrincipal().getName())) {
                return;
            }
            if(!Docs.LABSNEWS.type().equals(document.getType())){
                boolean authorized = labsBaseAdapter.isAuthorizedToDisplay(
                        userName,((LMNuxeoPrincipal) ctx.getPrincipal()).isAnonymous());
                authorized = authorized && !labsBaseAdapter.isDeleted();
                if (!authorized) {
                    throw new WebResourceNotFoundException(
                            userName
                                    + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT);
                }
            }
        } catch (ClientException e) {
            throw new WebResourceNotFoundException(
                    userName
                            + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT,
                    e);
        }
    }

    /**
     * @param document
     */
    private void initLabsBaseAdapter(DocumentModel document) {
        if (LabsSiteConstants.Docs.SITE.type().equals(document.getType())){
            labsBaseAdapter = document.getAdapter(LabsSite.class);
        }
        else{
            labsBaseAdapter = document.getAdapter(Page.class);
        }
    }
    
    public boolean isAuthorizedToDisplay(String user, boolean isAnomymous) throws ClientException{
        return labsBaseAdapter.isAuthorizedToDisplay(user, isAnomymous);
    }
    
    public boolean isAuthorizedToDisplay(String user, boolean isAnomymous, DocumentModel pDocument) throws ClientException{
        return pDocument.getAdapter(Page.class) != null ? pDocument.getAdapter(Page.class).isAuthorizedToDisplay(user, isAnomymous) : false;
    }
    
    public boolean isVisible() throws ClientException{
        return labsBaseAdapter.isVisible();
    }

    public Page getPage() throws ClientException {
        if (labsBaseAdapter instanceof LabsSiteAdapter){
            return ((LabsSiteAdapter) labsBaseAdapter).getIndexDocument().getAdapter(Page.class);
        }
        else{
            return (Page) labsBaseAdapter;
        }
    }
    
    @GET
    @Path(value="generated.less")
    public Object getGeneratedLess(){
        String themeName = "";
        try {
            LabsSite site = doc.getAdapter(SiteDocument.class).getSite();
            themeName = site.getThemeManager().getTheme().getName();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return getTemplate(GENERATED_LESS_TEMPLATE).arg("themeName", themeName);
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
            doc.setPropertyValue("dc:description", description);
            getCoreSession().saveDocument(doc);
            getCoreSession().save();
        } catch (Exception e) {
            Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Status.OK).build();
    }

    @POST
    @Path("@move")
    public Response doMove(@FormParam("destination") String destinationId, @FormParam("view") String view, @FormParam("redirect") String redirect) throws ClientException {
        DocumentModel destination = doc.getCoreSession().getDocument(new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath() + viewUrl + "?message_error=label.admin.page.move.destinationNotFolder");
        }
        try {
            doc.getCoreSession().move(doc.getRef(), destination.getRef(), null);
            doc.getCoreSession().save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error=" + e.getMessage());
        }
        return redirect(getPath() + viewUrl + "?message_success=label.admin.page.moved");
    }

    @POST
    @Path("@copy")
    public Response doCopy(@FormParam("destination") String destinationId, @FormParam("view") String view, @FormParam("redirect") String redirect) throws ClientException {
        DocumentModel destination = doc.getCoreSession().getDocument(new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath() + viewUrl + "?message_error=label.admin.page.copy.destinationNotFolder");
        }
        try {
            DocumentModel copy = doc.getCoreSession().copy(doc.getRef(), destination.getRef(), null);
            Page page = copy.getAdapter(Page.class);
            page.setTitle(COPYOF_PREFIX + page.getTitle());
            doc.getCoreSession().saveDocument(page.getDocument());
            doc.getCoreSession().save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error=" + e.getMessage());
        }
        return redirect(getPath() + viewUrl + "?message_success=label.admin.page.copied");
    }

    @POST
    @Path("@commentable")
    public Response doCommentable(){
        try {
            boolean isChecked = "on".equalsIgnoreCase(ctx.getForm().getString("commentablePage"));
            doc.getAdapter(Page.class).setCommentable(isChecked);
            CoreSession session = getCoreSession();
            session.saveDocument(doc);
            session.save();
        } catch (ClientException e) {
            return redirect(getPath() + "?message_error=label.parameters.page.save.fail");
        }
        return redirect(getPath() + "?message_success=label.parameters.page.save.success");
    }

    @GET
    @Path("@addContentView")
    public Template addContentView(){
        return getTemplate("views/LabsPage/manage.ftl");
    }
    
    @POST
    @Path("@addContent")
    public Response addContent() {
        String name = ctx.getForm().getString("name");
        DocumentModel newDoc = DocumentHelper.createDocument(ctx, doc,
                name);
        String pathSegment = URIUtils.quoteURIPathComponent(
                newDoc.getName(), true);
        return redirect(getPath() + '/' + pathSegment);

    }
    
    @PUT
    @Path("@setHome")
    public Response setAsHomePage() {
        LabsSite site;
        try {
            site = doc.getAdapter(SiteDocument.class).getSite();
            if (site.isAdministrator(ctx.getPrincipal().getName())) {
                site.setHomePageRef(doc.getId());
                doc.getCoreSession().saveDocument(site.getDocument());
            }
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return Response.noContent().build();
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

}
