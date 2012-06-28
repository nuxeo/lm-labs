package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

public abstract class CkEditorParametersAdapter extends DefaultAdapter {
	
	public CkEditorParametersAdapter() {
        WebContext ctx = WebEngine.getActiveContext();
        //callerRef
        String parameter = ctx.getRequest().getParameter("CKEditorFuncNum");
        if (StringUtils.isBlank(parameter)) {
            parameter = ctx.getRequest().getParameter("calledRef");
        }
        if (StringUtils.isNotBlank(parameter)) {
            ctx.getRequest().getSession().setAttribute("calledRef",parameter);
        }

        //jscallback
        parameter = ctx.getRequest().getParameter("callFunction");
        if (StringUtils.isBlank(parameter)) {
            parameter = "CKEDITOR.tools.callFunction";
        }
        ctx.getRequest().getSession().setAttribute("callFunction",parameter);
	}

    public String getCalledRef() {
        return (String) ctx.getRequest().getSession().getAttribute(
                "calledRef");
    }

    public String getCallFunction() {
        return (String) ctx.getRequest().getSession().getAttribute(
                "callFunction");
    }

}
