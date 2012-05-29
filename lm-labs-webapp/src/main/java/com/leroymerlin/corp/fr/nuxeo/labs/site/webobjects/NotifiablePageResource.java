package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageSubscription;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public abstract class NotifiablePageResource extends PageResource {

    private static final Log LOG = LogFactory.getLog(NotifiablePageResource.class);

    @GET
    @Path("@subscribe")
    public Response doSubscribe() {
        LOG.debug("@subscribe");
        try {
            PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
            subscriptionAdapter.subscribe(ctx.getPrincipal().getName());
            if (Docs.SITE.type().equals(doc.getType())) {
                List<Page> pages = doc.getAdapter(LabsSite.class).getAllPages(ctx.getCoreSession());
                for (Page page : pages) {
                    PageSubscription subscription = page.getDocument().getAdapter(PageSubscription.class);
                    if (subscription != null) {
                        subscription.subscribe(ctx.getPrincipal().getName());
                    } else {
                        LOG.warn("Unable to get adapter for " + page.getDocument().getPathAsString());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("@unsubscribe")
    public Object doUnsubscribe() {
        LOG.debug("@unsubscribe");
        try {
            PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
            subscriptionAdapter.unsubscribe(ctx.getPrincipal().getName());
            if (Docs.SITE.type().equals(doc.getType())) {
                CoreSession session = ctx.getCoreSession();
                for (Page page : doc.getAdapter(SiteDocument.class).getSite(session).getAllPages(session)) {
                    PageSubscription subscription = page.getDocument().getAdapter(PageSubscription.class);
                    if (subscription != null) {
                        subscription.unsubscribe(ctx.getPrincipal().getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        String redirect = ctx.getRequest().getParameter("redirect");
        if (BooleanUtils.toBoolean(redirect)) {
            return redirect(getPath()+ "?message_success=label.page.unsubscription.successful");
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
