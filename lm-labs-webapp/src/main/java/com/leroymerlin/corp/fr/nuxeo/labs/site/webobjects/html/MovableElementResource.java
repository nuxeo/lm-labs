package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.MovableElement;

public class MovableElementResource extends DocumentObject {

    protected MovableElement element = null;
    protected int index = -1;
    
    @GET
    @Path(value = "moveUpElement")
    public Object moveUp() {
        checkFields();
        try {
            element.moveUp(index);
            CoreSession session = ctx.getCoreSession();
            session.saveDocument(doc);
            session.save();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    @GET
    @Path(value = "moveDownElement")
    public Object moveDown() {
        checkFields();
        try {
            element.moveDown(index);
            CoreSession session = ctx.getCoreSession();
            session.saveDocument(doc);
            session.save();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }
    
    private void checkFields() throws WebException{
        if(element == null || index < 0){
            new WebException("Les champs sont mal initiés : element != null et index > -1");
        }
    }

}
