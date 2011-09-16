package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.Produces;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

@WebObject(type = "SiteTheme")
@Produces("text/html; charset=UTF-8")
public class SiteThemeResource extends DefaultObject{

    private LabsSite site;
    private SiteTheme theme;

    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 2;
        site = (LabsSite) args[0];
        theme = (SiteTheme) args[1];
    }



}
