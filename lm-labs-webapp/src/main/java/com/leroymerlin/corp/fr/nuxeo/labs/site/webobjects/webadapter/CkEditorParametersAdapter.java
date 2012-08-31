package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

public abstract class CkEditorParametersAdapter extends DefaultAdapter {
	
    public static final String PARAM_VALUE_CKEDITOR_CALLBACK = "CKEDITOR.tools.callFunction";
    public static final String PARAM_NAME_CALLBACK = "callFunction";
    public static final String PARAM_NAME_CALLED_REFERENCE = "calledRef";
    public static final String PARAM_NAME_CKEDITOR_CALLBACK = "CKEditorFuncNum";

    public CkEditorParametersAdapter() {
        WebContext ctx = WebEngine.getActiveContext();
        //callerRef
        String parameter = ctx.getRequest().getParameter(PARAM_NAME_CKEDITOR_CALLBACK);
        if (StringUtils.isBlank(parameter)) {
            parameter = ctx.getRequest().getParameter(PARAM_NAME_CALLED_REFERENCE);
        }
        if (StringUtils.isNotBlank(parameter)) {
            ctx.getRequest().getSession().setAttribute(PARAM_NAME_CALLED_REFERENCE,parameter);
        }

        //jscallback
        parameter = ctx.getRequest().getParameter(PARAM_NAME_CALLBACK);
        if (StringUtils.isBlank(parameter)) {
            parameter = PARAM_VALUE_CKEDITOR_CALLBACK;
        }
        ctx.getRequest().getSession().setAttribute(PARAM_NAME_CALLBACK,parameter);
	}

    public String getCalledRef() {
        return (String) ctx.getRequest().getSession().getAttribute(
                PARAM_NAME_CALLED_REFERENCE);
    }

    public String getCallFunction() {
        return (String) ctx.getRequest().getSession().getAttribute(
                PARAM_NAME_CALLBACK);
    }

}
