package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.nuxeo.ecm.webengine.WebEngine.SKIN_PATH_PREFIX_KEY;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.webengine.model.Module;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.providers.LatestUploadsPageProvider;

public final class LabsSiteWebAppUtils {

    public static final String DEFAULT_BANNER = "/images/default_banner.png";

    private static final String LATEST_UPLOADS_PAGEPROVIDER = "latest_uploads";

    public static final String DIRECTORY_TEMPLATE = "/skin/views/TemplatesBase";

    public static final String DIRECTORY_THEME = "/skin/resources/less/theme";
    
    public static String NUXEO_WEBENGINE_BASE_PATH = "nuxeo-webengine-base-path";

    private LabsSiteWebAppUtils() {
    }

    public static PageProvider<DocumentModel> getLatestUploadsPageProvider(
            DocumentModel doc, long pageSize) throws Exception {
        PageProviderService ppService = Framework.getService(PageProviderService.class);
        List<SortInfo> sortInfos = null;
        Map<String, Serializable> props = new HashMap<String, Serializable>();

        SiteDocument sd = doc.getAdapter(SiteDocument.class);

        props.put(
                LatestUploadsPageProvider.PARENT_DOCUMENT_PROPERTY,
                (Serializable) sd.getSite().getTree());
        @SuppressWarnings("unchecked")
        PageProvider<DocumentModel> pp = (PageProvider<DocumentModel>) ppService.getPageProvider(
                LATEST_UPLOADS_PAGEPROVIDER, sortInfos, new Long(pageSize),
                null, props, "");
        return pp;
    }

    public static List<String> getFoldersUnderFolder(String path) {
        File directoryBase = new File(path);
        return Arrays.asList(directoryBase.list(new DirectoryFilter()));
    }
    
    /**
     * to complete with '/'
     * @param module of webengine
     * @param ctx the webContext
     * @return
     */
    public static String getSkinPathPrefix(Module module, WebContext ctx) {
        if (Framework.getProperty(SKIN_PATH_PREFIX_KEY) != null) {
            return module.getSkinPathPrefix();
        }
        String webenginePath = ctx.getRequest()
                .getHeader(NUXEO_WEBENGINE_BASE_PATH);
        if (webenginePath == null) {
            return module.getSkinPathPrefix();
        } else {
            return ctx.getBasePath() + "/" + module.getName();
        }
    }
    
    public static String getPathDefaultBanner(Module module, WebContext ctx) {
        return getSkinPathPrefix(module, ctx) + DEFAULT_BANNER;
    }
    

}
