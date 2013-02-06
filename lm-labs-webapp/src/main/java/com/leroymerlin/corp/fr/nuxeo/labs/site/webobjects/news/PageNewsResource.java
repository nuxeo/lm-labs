/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.news;


import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsBlobHolderException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNewsAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageNewsResource extends NotifiablePageResource {

    private static final Log log = LogFactory.getLog(PageNewsResource.class);

    private PageProvider<DocumentModel> newsPageProvider;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        initNewsPageProvider();
        ctx.getEngine()
                .getRendering()
                .setSharedVariable("pageNews", Tools.getAdapter(PageNews.class, doc, ctx.getCoreSession()));

    }
    
    @GET
    @Path("@labsNewsTemplateOfSiteElementTemplate")
    public Template getLabsNewsTemplateOfSiteElementTemplate(){
    	String property = getProperty("labs.site.element.template.url", "modeles-pages");
    	try {
			LabsSite siteTemplate = getSiteManager().getSite(ctx.getCoreSession(), property);
			return getTemplate("views/LabsPage/pageTemplate.ftl").arg("pages", siteTemplate.getAllLabsNewsTemplate());
		} catch (Exception e) {
			throw WebException.wrap(e);
		}
    }

    @GET
    @Path("@labsNewsTemplateOfSite")
    public Template getLabsNewsTemplateOfSite(){
    	try {
    		LabsSite site = CommonHelper.siteDoc(doc).getSite();
			return getTemplate("views/LabsPage/pageTemplate.ftl").arg("pages", site.getAllLabsNewsTemplate());
		} catch (Exception e) {
			throw WebException.wrap(e);
		}
    }

    @Override
    @POST
    public Response doPost() {
        LabsNews news = null;
        try {
            FormData form = ctx.getForm();
            String pTitle = form.getString("dc:title");
            CoreSession session = ctx.getCoreSession();
            PageNews pageNews = Tools.getAdapter(PageNews.class, doc, session);

            
            if("assistant".equalsIgnoreCase(form.getString("assistant"))){
                final String idRefPage = form.getString("idPageTemplate");
                if (!StringUtils.isEmpty(idRefPage)){
                    DocumentModel copynews = LabsSiteUtils.copyHierarchyPage(new IdRef(idRefPage), doc.getRef(), pTitle, pTitle, session, false);
                    news = Tools.getAdapter(LabsNews.class, copynews, session);
                }
            }
            
            if(news == null){
            	news = pageNews.createNews(pTitle);
            }
            LabsNewsResource.fillNews(form, news, session);

            DocumentModel newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath() + "/" + newDocNews.getName() + "?props=open");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        } catch (IOException e) {
            throw WebException.wrap(e);
        }catch (LabsBlobHolderException e) {
            log.info("The size of blob is too small !", e);
            DocumentModel newDocNews = save(news);
            return redirect(getPath() + "/" + newDocNews.getName()
                    + "?message_success=label.labsNews.news_notupdated.size&props=open");
        }

    }

    private DocumentModel save(LabsNews news){
        CoreSession session = ctx.getCoreSession();
        DocumentModel newDocNews = null;
        try {
            newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return newDocNews;
    }

    public String getSamplePictureHtml() {
        return "<img src=\"" + LabsSiteWebAppUtils.getSkinPathPrefix(getModule(), getContext()) + "/images/defaultNews.jpg\"/>";
    }

    public List<LabsNews> getAllNews(List<DocumentModel> docs) throws ClientException{
        List<LabsNews> result = new ArrayList<LabsNews>();
        LabsNewsAdapter adapter = null;
        for (DocumentModel document : docs){
            adapter = (LabsNewsAdapter) Tools.getAdapter(LabsNews.class, document, ctx.getCoreSession());
            if (adapter != null){
                result.add(adapter);
            }
        }
        return result;
    }

	public PageProvider<DocumentModel> getNewsPageProvider() {
		return newsPageProvider;
	}

	@SuppressWarnings("unused")
    private void logNewsPageProvider() {
		if (log.isDebugEnabled()) {
			final String logPrefix = "<getNewsPageProvider> ";
			try {
				log.debug(logPrefix + "currentEntry: " + newsPageProvider.getCurrentEntry().getTitle());
				log.debug(logPrefix + "pageSize: " + newsPageProvider.getPageSize());
				log.debug(logPrefix + "isNextEntryAvailable: " + newsPageProvider.isNextEntryAvailable());
				if (newsPageProvider.isNextEntryAvailable()) {
					log.debug(logPrefix + "currentEntry: " + "");
				}
				log.debug(logPrefix + "isPreviousEntryAvailable: " + newsPageProvider.isPreviousEntryAvailable());
			} catch (ClientException e) {
				log.error(logPrefix);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initNewsPageProvider() {
		List<SortInfo> sortInfos = null;
		Map<String, Serializable> props = new HashMap<String, Serializable>();
		props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY,
				(Serializable) getCoreSession());
		String coreQueryPageProviderName = "";
		Object[] paramQuery = null;
		boolean canWrite = false;
		try {
			canWrite = getCoreSession().hasPermission(doc.getRef(),SecurityConstants.WRITE);
		} catch (ClientException e) {
			log.error(e, e);
		}
		if (canWrite){
			coreQueryPageProviderName = "list_news_write_nxql";
			paramQuery = new Object[] { doc.getId() };
		} else {
			coreQueryPageProviderName = "list_news_nxql";
			String date_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			paramQuery = new Object[] { doc.getId(), date_str, date_str };
		}
		try {
			PageProviderService ppService = Framework.getService(PageProviderService.class);
			this.newsPageProvider = (PageProvider<DocumentModel>) ppService.getPageProvider(
					coreQueryPageProviderName, sortInfos, new Long(getPage().getElementsPerPage()),
					null, props, paramQuery);
		} catch (ClientException e) {
			log.error(e, e);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

    @Override
    public Resource traverse(String path) {
    	try {
    		PathRef pathRef = new PathRef(doc.getPath().append(path).toString());
    		DocumentModel currentNewsDoc = ctx.getCoreSession().getDocument(pathRef);
    		if (Docs.LABSNEWS.type().equals(currentNewsDoc.getType())) {
    			return (DocumentObject) ctx.newObject(currentNewsDoc.getType(), currentNewsDoc, getNewsPageProvider());
    		} else {
    			return super.traverse(path);
    		}
    	} catch (Exception e) {
    		throw WebException.wrap(e);
    	}
    }

}
