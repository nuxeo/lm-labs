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

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@WebObject(type = "SiteTheme")
@Produces("text/html; charset=UTF-8")
public class SiteThemeResource extends PageResource {

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

    public List<Enum<LabsSiteConstants.Template>> getTemplates() {
        List<Enum<LabsSiteConstants.Template>> entriesTemplate = new ArrayList<Enum<LabsSiteConstants.Template>>();
        for (Enum<LabsSiteConstants.Template> template : LabsSiteConstants.Template.values()) {
            entriesTemplate.add(template);
        }
        if (entriesTemplate.isEmpty()){
            LOG.error(THE_TEMPLATES_SHOULD_NOT_BE_EMPTY);
        }
        return entriesTemplate;
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
                        + "?message_success=label.theme.banner_updated");
            } catch (ClientException e) {
                throw WebException.wrap(e);
            }
        }
        return redirect(getPath()
                + "?message_warning=label.theme.nothing_changed");
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
                        + "?message_success=label.template.updated");
            } catch (ClientException e) {
                throw WebException.wrap(e);
            }
    }

}
