package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.rendering.fm.adapters.DocumentTemplate;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class LatestUploadsPageProviderTemplateMethod implements TemplateMethodModelEx {

    private static final Log LOG = LogFactory.getLog(LatestUploadsPageProviderTemplateMethod.class);

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 3) {
            throw new TemplateModelException(
                    "LatestUploads takes THREE parameters !");
        }
        DocumentTemplate a = (DocumentTemplate) arguments.get(0);
        DocumentModel document = a.getDocument();
        long pageSize = ((SimpleNumber) arguments.get(1)).getAsNumber().longValue();
        CoreSession session = (CoreSession)((StringModel) arguments.get(2)).getWrappedObject();
        try {
            return LabsSiteWebAppUtils.getLatestUploadsPageProvider(document, pageSize, session);
        } catch (Exception e) {
            LOG.error(e, e);
            throw new TemplateModelException("Unable to get page provider.");
        }
    }
}
