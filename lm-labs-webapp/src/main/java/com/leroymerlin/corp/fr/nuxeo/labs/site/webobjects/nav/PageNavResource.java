package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.nav;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.nav.PageNav;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;

@WebObject(type = "PageNav", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageNavResource extends NotifiablePageResource{

    private static final Log log = LogFactory.getLog(PageNavResource.class);

    private PageProvider<DocumentModel> taggedPageProvider;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        initTaggedPageProvider();
    }

    public PageProvider<DocumentModel> getTaggetPageProvider() {
        return taggedPageProvider;
    }

    public List<Page> getTaggedPage(List<DocumentModel> docs) throws ClientException{
        List<Page> result = new ArrayList<Page>();
        if (docs != null){
            Page adapter = null;
            for (DocumentModel document : docs){
                adapter = Tools.getAdapter(Page.class, document, ctx.getCoreSession());
                if (adapter != null){
                    result.add(adapter);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void initTaggedPageProvider() {
        List<SortInfo> sortInfos = null;
        Map<String, Serializable> props = new HashMap<String, Serializable>();
        props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY,
                (Serializable) getCoreSession());
        String coreQueryPageProviderName = "";
        Object[] paramQuery = null;

        DocumentModel document = getDocument();
        CoreSession session = ctx.getCoreSession();
        PageNav pageNav = Tools.getAdapter(PageNav.class, document, session);
        SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, doc, session);
        coreQueryPageProviderName = "list_taggedpage_nxql";
        try {
            if (!StringUtils.isEmpty(pageNav.getUserQuery())){
                coreQueryPageProviderName = "empty_pattern_nxql";
            }
            else{
                paramQuery = new Object[] { siteDocument.getSite().getTree().getPathAsString(), pageNav.getTags() };
            }
        } catch (ClientException e) {
            log.error(e, e);
        }

        try {
            PageProviderService ppService = Framework.getService(PageProviderService.class);
            this.taggedPageProvider = (PageProvider<DocumentModel>) ppService.getPageProvider(
                    coreQueryPageProviderName, sortInfos, new Long(getPage().getElementsPerPage()),
                    null, props, paramQuery);
            if ("empty_pattern_nxql".equals(coreQueryPageProviderName)){
                this.taggedPageProvider.getDefinition().setPattern(pageNav.getUserQuery());
            }
        } catch (ClientException e) {
            log.error(e, e);
        } catch (Exception e) {
            log.error(e, e);
        }
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
            String userQuery = form.getString("userQuery");
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
                pageNav.setUserQuery(userQuery);
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
        String pathView = WebEngine.getActiveContext().getModule().getRoot().getAbsolutePath() + "/skin/views/" 
            + page.getDocument().getType() + "/previewNav.ftl";
        File file = new File(pathView);
        if (file.exists()){
            return true;
        }
        return false;
    }
}
