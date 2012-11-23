package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.nav;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.TemplateNotFoundException;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.nav.PageNav;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;

@WebObject(type = "PageNav", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageNavResource extends NotifiablePageResource{

    @Override
    public Resource initialize(WebContext ctx, ResourceType type,
            Object... args) {
        return super.initialize(ctx, type, args);
    }
    
    @Override
    @GET
    public Object doGet() {
        DocumentModel document = getDocument();
        CoreSession session = ctx.getCoreSession();
        PageNav pageNav = Tools.getAdapter(PageNav.class, document, session);
        if (pageNav == null){
            throw new WebException("No PageNav");
        }
        try {
            return getView("index").arg("pageNav", pageNav).arg("allSiteTags", getAllSiteTags())
                    .arg("taggedPages", getTaggedPages());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }
    
    private Collection<String> getAllSiteTags() throws ClientException{
        Set<String> tags = new HashSet<String>();
        DocumentModel document = getDocument();
        CoreSession session = ctx.getCoreSession();
        SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, document, session);
        if (siteDocument != null){
            LabsSite site = siteDocument.getSite();
            if (site != null){
                for (Page page:site.getAllPages()){
                    for (String tag:page.getLabsTags()){
                        tags.add(tag.toLowerCase());
                    }
                }
            }
        }
        return tags;
    }
    
    private Collection<Page> getTaggedPages() throws ClientException {
        List<Page> pages = new ArrayList<Page>();
        DocumentModel document = getDocument();
        CoreSession session = ctx.getCoreSession();
        PageNav pageNav = Tools.getAdapter(PageNav.class, document, session);
        if (pageNav != null){
            pages = pageNav.getTaggetPages();
        }
        return pages;
    }

    @Override
    @POST
    public Response doPost() {
        try {
            FormData form = ctx.getForm();
            String listTags = form.getString("listTags");
            String[] split = listTags.split(",");
            if (split.length ==1 && StringUtils.isEmpty(split[0])){
                split = new String[0];
            }
            List<String> list = new ArrayList<String>(Arrays.asList(split));
            DocumentModel document = getDocument();
            CoreSession session = ctx.getCoreSession();
            PageNav pageNav = Tools.getAdapter(PageNav.class, document, session);
            if (pageNav != null) {
                pageNav.setTags(list);
                session.saveDocument(document);
                session.save();
                return Response.ok().build();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        return Response.status(Status.NOT_MODIFIED).build();
    }
    
    public boolean pageAsPreview(Page page){
        try {
            getView("views/" + page.getDocument().getType() + "/previewNav");
        } catch (TemplateNotFoundException e) {
            return false;
        }
        return true;
    }
}
