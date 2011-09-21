package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.rendering.fm.adapters.DocumentTemplate;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class PageEndUrlTemplateMethod implements TemplateMethodModelEx {

    private static final Log LOG = LogFactory.getLog(PageEndUrlTemplateMethod.class);

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException(
                    "BreadcrumbsArray takes only ONE parameter !");
        }
        DocumentTemplate a = (DocumentTemplate) arguments.get(0);
        DocumentModel document = a.getDocument();
        if(document == null) {
            return "";
        }
        try {
            return LabsSiteWebAppUtils.buildEndUrl(document);
        } catch (ClientException e) {
            LOG.error(e, e);
            return "";
        }
    }

}
