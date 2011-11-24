package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.services.LabsThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

@WebObject(type = "SiteTheme")
@Produces("text/html; charset=UTF-8")
public class SiteThemeResource extends PageResource {

    private static final String THE_THEMES_SHOULD_NOT_BE_EMPTY = "The themes should not be empty !";

    private static final String THE_TEMPLATES_SHOULD_NOT_BE_EMPTY = "The templates should not be empty !";

    private static final Log LOG = LogFactory.getLog(SiteThemeResource.class);

    private LabsSite site;
    private SiteTheme theme;

    @Override
    public void initialize(Object... args) {
        assert args != null && args.length == 2;
        site = (LabsSite) args[0];
        theme = (SiteTheme) args[1];
        doc = theme.getDocument();
    }
    
    @Override
    public boolean isVisible() throws ClientException{
        return true;
    }
    
    @Override
    public Page getPage() throws ClientException{
        return null;
    }

    public List<String> getTemplates(){
        LabsThemeManager themeService = getThemeService();
        List<String> entriesTemplate = new ArrayList<String>();
        if (themeService != null){
            entriesTemplate = themeService.getTemplateList(getModule().getRoot().getAbsolutePath());
        }
        if (entriesTemplate.isEmpty()){
            LOG.error(THE_THEMES_SHOULD_NOT_BE_EMPTY);
            LOG.error("Verify the package " + LabsSiteWebAppUtils.DIRECTORY_THEME);
        }
        return entriesTemplate;
    }

    /**
     * @throws Exception
     */
    private LabsThemeManager getThemeService() {
        try {
            return Framework.getService(LabsThemeManager.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getThemes() {
        LabsThemeManager themeService = getThemeService();
        List<String> entriesTheme = new ArrayList<String>();
        if (themeService != null){
            entriesTheme = themeService.getThemeList(getModule().getRoot().getAbsolutePath());
        }
        if (entriesTheme.isEmpty()){
            LOG.error(THE_TEMPLATES_SHOULD_NOT_BE_EMPTY);
            LOG.error("Verify the package " + LabsSiteWebAppUtils.DIRECTORY_TEMPLATE);
        }
        return entriesTheme;
    }

    @GET
    public Template doGet() {
        return getView("index");
    }

    @POST
    public Response doPost() {
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            Blob blob = form.getFirstBlob();
            try {
                site.setLogo(blob);
                CoreSession session = ctx.getCoreSession();
                session.saveDocument(site.getDocument());
                session.save();
                return redirect(getPath()
                        + "?message_success=label.labssites.banner_updated");
            } catch (ClientException e) {
                throw WebException.wrap(e);
            }
        }
        return redirect(getPath()
                + "?message_warning=label.labssites.banner_nothing_changed");
    }

    @POST
    @Path(value="template")
    public Response doPostTemplate() {
        FormData form = ctx.getForm();
        try {
            CoreSession session = ctx.getCoreSession();
            site.getTemplate().setTemplateName(form.getString("template"));
            session.saveDocument(site.getDocument());
            session.save();
            return redirect(getPath()
                    + "?message_success=label.labssites.template.updated");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    @POST
    @Path(value="theme")
    public Response doPostTheme() {
        FormData form = ctx.getForm();
        try {
            site.getThemeManager().setTheme(form.getString("theme"));
            return redirect(getPath()
                    + "?message_success=label.labssites.theme.updated");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

}
