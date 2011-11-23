package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

@WebAdapter(name = "pageUtils", type = "PageUtilsService")
public class PageUtilsService extends DefaultAdapter {

    public static final String COPYOF_PREFIX = "Copie de ";

    @POST
    @Path("move")
    public Response doAdminMove(@FormParam("source") String sourceId,
            @FormParam("destination") String destinationId,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {
        DocumentModel doc = this.getTarget().getAdapter(DocumentModel.class);
        DocumentModel source = doc.getCoreSession().getDocument(
                new IdRef(sourceId));
        DocumentModel sourceParent = doc.getCoreSession().getParentDocument(source.getRef());
        if (sourceParent.getId().equals(destinationId)) {
            return Response.noContent().build();
        }
        DocumentModel destination = doc.getCoreSession().getDocument(
                new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath()
                        + viewUrl
                        + "?message_error=label.admin.page.move.destinationNotFolder");
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).build();
            }
        }
        try {
            DocumentModel move = doc.getCoreSession().move(source.getRef(),
                    destination.getRef(), source.getTitle());
            doc.getCoreSession().save();
            if (BooleanUtils.toBoolean(redirect)) {
                return redirect(getPath() + viewUrl
                        + "?message_success=label.admin.page.moved");
            } else {
                return Response.ok().entity(
                        move.getAdapter(Page.class).getTitle()).build();
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
    @Path("copy")
    public Response doAdminCopy(@FormParam("source") String sourceId,
            @FormParam("destination") String destinationId,
            @FormParam("redirect") String redirect,
            @FormParam("view") String view) throws ClientException {
        DocumentModel doc = this.getTarget().getAdapter(DocumentModel.class);
        DocumentModel source = doc.getCoreSession().getDocument(
                new IdRef(sourceId));
        DocumentModel destination = doc.getCoreSession().getDocument(
                new IdRef(destinationId));
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
            DocumentModel copy = doc.getCoreSession().copy(source.getRef(),
                    destination.getRef(), null);
            Page page = copy.getAdapter(Page.class);
            String newTitle = COPYOF_PREFIX + page.getTitle();
            page.setTitle(newTitle);
            doc.getCoreSession().saveDocument(page.getDocument());
            doc.getCoreSession().save();
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
        DocumentModel doc = this.getTarget().getAdapter(DocumentModel.class);
        DocumentModel source = doc.getCoreSession().getDocument(
                new IdRef(sourceId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        try {
            Page page = source.getAdapter(Page.class);
            page.setTitle(title);
            doc.getCoreSession().saveDocument(page.getDocument());
            doc.getCoreSession().save();
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
                    destination.getPathAsString(), title, doctype);
            newDoc = session.createDocument(newDoc);
            session.saveDocument(newDoc);
            session.save();
        } catch (ClientException e) {
            Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Status.OK).build();
    }
}
