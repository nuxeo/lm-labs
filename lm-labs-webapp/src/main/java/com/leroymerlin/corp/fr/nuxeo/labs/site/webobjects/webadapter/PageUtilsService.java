package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@WebAdapter(name = "pageUtils", type = "PageUtilsService")
public class PageUtilsService extends DefaultAdapter {

    public static final String COPYOF_PREFIX = "Copie de ";

    static final String[] ADMIN_MSG = { "label.admin.page.moved",
            "label.admin.page.notMoved",
            "label.admin.page.move.destinationNotFolder" };

    static final String[] PAGE_CLASSEUR_MSG = {
            "label.PageClasseur.folder.moved",
            "label.PageClasseur.folder.notMoved",
            "label.PageClasseur.folder.copy.destinationNotFolder" };

    static final String[] PAGE_CLASSEUR_ELMENTS_MSG = {
            "label.PageClasseur.moveElements.moved",
            "label.PageClasseur.moveElements.notMoved",
            "label.PageClasseur.moveElements.move.destinationNotFolder" };

    @POST
    @Path("move")
    public Response doAdminMove(@FormParam("source") String sourceId,
            @FormParam("destinationContainer") String destinationId,
            @FormParam("after") String afterId,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {
        return doMove(sourceId, destinationId, afterId, redirect, view,
                ADMIN_MSG);
    }

    @POST
    @Path("bulkMove")
    public Response doBulkMove(@QueryParam("id")  List<String> ids,
            @QueryParam("destinationContainer") String destinationId){
        CoreSession session = ctx.getCoreSession();
        DocumentModel source = null;
        try {
            DocumentModel destination = session.getDocument(new IdRef(destinationId));
            if (!destination.isFolder()) {
                return Response.ok("?message_error=" + PAGE_CLASSEUR_ELMENTS_MSG[2]).build();
            }
            for (String id : ids){
                source = session.getDocument(new IdRef(id));
                session.move(new IdRef(id), destination.getRef(), source.getTitle());
            }
            session.save();
        } catch (Exception e) {
            return Response.ok("?message_error=" + PAGE_CLASSEUR_ELMENTS_MSG[1]).build();
        }
        return  Response.ok("?message_success=" + PAGE_CLASSEUR_ELMENTS_MSG[0]).build();
    }

    @POST
    @Path("moveFolder")
    public Response doFolderMove(@FormParam("source") String sourceId,
            @FormParam("destinationContainer") String destinationId,
            @FormParam("after") String afterId,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {
        return doMove(sourceId, destinationId, afterId, redirect, view,
                PAGE_CLASSEUR_MSG);
    }

    private Response doMove(final String sourceId, final String destinationId,
            final String afterId, final String redirect, final String view,
            final String[] msg) throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel source = session.getDocument(new IdRef(sourceId));
        DocumentModel sourceParent = session.getParentDocument(source.getRef());
        DocumentModel destination = session.getDocument(new IdRef(destinationId));

        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath() + viewUrl + "?message_error="
                        + msg[2]);
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).build();
            }
        }
        try {
            DocumentModel toMoved = null;
            if (sourceParent.getId().equals(destinationId)) {
                toMoved = session.getDocument(new IdRef(sourceId));
            } else {
                toMoved = session.move(source.getRef(), destination.getRef(),
                        null);
            }
            DocumentModel after = null;
            if (!StringUtils.isEmpty(afterId)) {
                after = session.getDocument(new IdRef(afterId));
                session.orderBefore(destination.getRef(), toMoved.getName(),
                        after.getName());
            } else {
                session.orderBefore(destination.getRef(), toMoved.getName(),
                        null);
            }
            session.save();

            if (BooleanUtils.toBoolean(redirect)) {
                return Response.ok("?message_success=" + msg[0]).build();
            } else {
                Page page = Tools.getAdapter(Page.class, toMoved, session);
                if (page != null) {
                    return Response.ok().entity(page.getTitle()).build();
                } else {
                    return Response.ok().build();
                }
            }
        } catch (Exception e) {
            if (BooleanUtils.toBoolean(redirect)) {
                return Response.ok("?message_error=" + msg[1]).build();
            } else {
                return Response.status(Status.PRECONDITION_FAILED).build();
            }
        }
    }

    @POST
    @Path("copy")
    public Response doAdminCopy(@FormParam("source") String sourceId,
            @FormParam("destinationContainer") String destinationId,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {

        CoreSession session = ctx.getCoreSession();
        DocumentModel source = session.getDocument(new IdRef(sourceId));
        DocumentModel destination = session.getDocument(new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath()
                        + viewUrl
                        + "?message_error=label.admin.page.copy.destinationNotFolder");
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).build();
            }
        }
        try {
            String newTitle = COPYOF_PREFIX + source.getTitle();
            LabsSiteUtils.copyHierarchyPage(source.getRef(), destination.getRef(), newTitle, newTitle, session, false);
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath() + viewUrl
                        + "?message_success=label.admin.page.copied");
            } else {
                return Response.ok().entity(newTitle).build();
            }
        } catch (Exception e) {
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath() + viewUrl + "?message_error="
                        + e.getCause());
            } else {
                return Response.status(Status.PRECONDITION_FAILED).build();
            }
        }
    }

    @POST
    @Path("rename")
    public Response doAdminRename(@FormParam("source") String sourceId,
            @FormParam("newTitle") String title,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel source = session.getDocument(
                new IdRef(sourceId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        try {
            Page page = Tools.getAdapter(Page.class, source, session);
            page.setTitle(title);
            session.saveDocument(page.getDocument());
            session.save();
        } catch (Exception e) {
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath() + viewUrl + "?message_error="
                        + e.getCause());
            } else {
                return Response.status(Status.PRECONDITION_FAILED).build();
            }
        }
        if (BooleanUtils.toBoolean(redirect)) {
            return redirect(getPath() + viewUrl
                    + "?message_success=label.admin.page.renamed");
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Path("addFolder")
    public Response doAdminAddFolder(@FormParam("title") String title,
            @FormParam("doctype") String doctype,
            @FormParam("destination") String destinationId) {
        if (StringUtils.isBlank(title) || StringUtils.isBlank(doctype)
                || StringUtils.isBlank(destinationId)) {
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        CoreSession session = getContext().getCoreSession();

        try {
            DocumentModel destination = session.getDocument(new IdRef(
                    destinationId));
            DocumentModel newDoc = getContext().getCoreSession().createDocumentModel(
                    destination.getPathAsString(), LabsSiteUtils.doLabsSlugify(title), doctype);
            newDoc = session.createDocument(newDoc);
            session.saveDocument(newDoc);
            newDoc.setPropertyValue("dc:title", title);
            session.saveDocument(newDoc);
            session.save();
        } catch (ClientException e) {
            Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Status.OK).build();
    }
}
