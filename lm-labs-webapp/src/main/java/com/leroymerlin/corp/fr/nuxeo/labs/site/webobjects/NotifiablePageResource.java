package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageSubscription;

public abstract class NotifiablePageResource extends PageResource {

    private static final Log LOG = LogFactory.getLog(NotifiablePageResource.class);

    @GET
    @Path("@subscribe")
    public Response doSubscribe() {
        LOG.debug("@subscribe");
        try {
            PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
            if (!subscriptionAdapter.isSubscribed(ctx.getPrincipal().getName())) {
                subscriptionAdapter.subscribe(ctx.getPrincipal().getName());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e, e);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("@unsubscribe")
    public Response doUnsubscribe() {
        LOG.debug("@unsubscribe");
        try {
            PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
            subscriptionAdapter.unsubscribe(ctx.getPrincipal().getName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e, e);
        }
        return Response.noContent().build();
    }
    
    @GET
    @Path("@subscribed")
    public Response doSubscribed() {
        return Response.ok().entity(isSubscribed().toString()).build();
    }
    
    public Boolean isSubscribed() {
        try {
            PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
            return subscriptionAdapter.isSubscribed(ctx.getPrincipal().getName());
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }
    
}
