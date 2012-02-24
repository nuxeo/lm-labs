package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;

@WebObject(type = "PageClasseur", superType = "LabsPage")
public class PageClasseurResource extends NotifiablePageResource {

    private static final Log LOG = LogFactory.getLog(PageClasseurResource.class);

    private PageClasseur classeur;

    @Deprecated
    private ConversionService conversionService = null;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        classeur = doc.getAdapter(PageClasseur.class);
        ctx.getEngine().getRendering().setSharedVariable("classeur", classeur);
    }

    @GET
    public Object doGet() {
        return getView("index");
    }

    public boolean hasConvertersForHtml(String mimetype) throws Exception {
        if (mimetype.startsWith("image")) {
            return true;
        }
        getConversionService();
        List<String> converterNames = getConversionService().getConverterNames(
                mimetype, "text/html");
        if (converterNames.size() < 1) {
            return false;
        }
        for (String converterName : converterNames) {
            if (!getConversionService().isConverterAvailable(converterName).isAvailable()) {
                return false;
            }
        }
        return true;
    }

    private ConversionService getConversionService() throws Exception {
        if (conversionService == null) {
            conversionService = Framework.getService(ConversionService.class);
        }
        return conversionService;
    }

    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        String folderTitle = form.getString("dc:title");
        if (!StringUtils.isEmpty(folderTitle)) {
            try {
                classeur.addFolder(folderTitle);
                getCoreSession().save();
                return Response.status(Status.OK).build();
            } catch (ClientException e) {
                return Response.serverError().status(Status.FORBIDDEN).entity(
                        e.getMessage()).build();
            }
        } else {
            return Response.serverError().status(Status.FORBIDDEN).entity(
                    "Folder name is empty").build();
        }
    }

    // @POST
    // @Path("@rename/{id}")
    // public Response doRename(@PathParam("id") String idRef) {
    // FormData form = ctx.getForm();
    // String folderTitle = form.getString("folderName");
    // if (!StringUtils.isEmpty(folderTitle)) {
    // try {
    // classeur.renameFolder(idRef, folderTitle);
    // getCoreSession().save();
    // return Response.status(Status.OK).build();
    // } catch (ClientException e) {
    // return Response.serverError().status(Status.FORBIDDEN).entity(
    // e.getMessage()).build();
    // }
    //
    // } else {
    // return Response.serverError().status(Status.FORBIDDEN).entity(
    // "Folder name is empty").build();
    // }
    // }

    @Path(value = "{path}")
    @Override
    public Resource traverse(@PathParam("path") String path) {
        try {
            PathRef pathRef = new PathRef(
                    doc.getPath().append(path).toString());
            DocumentModel subDoc = ctx.getCoreSession().getDocument(pathRef);
            if (Docs.pageDocs().contains(Docs.fromString(subDoc.getType()))) {
                return (DocumentObject) ctx.newObject(subDoc.getType(), subDoc);
            } else if (Docs.PAGECLASSEURFOLDER.type().equals(subDoc.getType())) {
                return newObject("PageClasseurFolder", subDoc);
            } else {
                throw new WebResourceNotFoundException(
                        "Unknow sub-type for a PageClasseur: "
                                + subDoc.getType());
            }
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    public BlobHolder getBlobHolder(final DocumentModel document) {
        return document.getAdapter(BlobHolder.class);
    }

    @DELETE
    @Path("bulk")
    public Response doBulkDelete(@QueryParam("id") List<String> ids) {
        final String logPrefix = "<doBulkDelete> ";
        LOG.debug(logPrefix);
        try {
            boolean removed = false;
            for (String id : ids) {
                LOG.debug(id);
                IdRef idRef = new IdRef(id);
                if (getCoreSession().exists(idRef)) {
                    DocumentModel file = getCoreSession().getDocument(idRef);
                    if (file.getAllowedStateTransitions().contains(
                            LifeCycleConstants.DELETE_TRANSITION)) {
                        file.followTransition(LifeCycleConstants.DELETE_TRANSITION);
                        getCoreSession().saveDocument(file);
                        removed = true;
                    }
                }
            }
            if (removed) {
                getCoreSession().save();
            }
        } catch (ClientException e) {
            LOG.error(e.getMessage());
            return Response.serverError().status(Status.NOT_MODIFIED).entity(
                    e.getMessage()).build();
        }
        return Response.status(Status.NO_CONTENT).build();
    }

    @PUT
    @Path("@filesVisibility/{action}")
    public Response doSetFilesVisibility(@PathParam("action") String action,
            @QueryParam("id") List<String> ids) throws ClientException {
        final String logPrefix = "<doSetFilesVisibility> ";
        LOG.debug(logPrefix);
        boolean modified = false;
        try {
            for (String id : ids) {
                IdRef idRef = new IdRef(id);
                if (getCoreSession().exists(idRef)) {
                    DocumentModel file = getCoreSession().getDocument(idRef);
                    DocumentModel folderDoc = getCoreSession().getParentDocument(
                            file.getRef());
                    PageClasseurFolder folder = folderDoc.getAdapter(PageClasseurFolder.class);
                    boolean done = false;
                    if (folder != null) {
                        if ("hide".equals(action)) {
                            done = folder.hide(file);
                        } else if ("show".equals(action)) {
                            done = folder.show(file);
                        }
                    }
                    modified |= done;
                }
            }
        } catch (ClientException e) {
            LOG.error(e.getMessage());
            return Response.serverError().status(Status.NOT_MODIFIED).entity(
                    e.getMessage()).build();
        }
        if (modified) {
            return Response.noContent().build();
        } else {
            return Response.notModified().build();
        }
    }

}
