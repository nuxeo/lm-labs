package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

@WebObject(type = "SiteTheme")
@Produces("text/html; charset=UTF-8")
public class SiteThemeResource extends PageResource {

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

}
