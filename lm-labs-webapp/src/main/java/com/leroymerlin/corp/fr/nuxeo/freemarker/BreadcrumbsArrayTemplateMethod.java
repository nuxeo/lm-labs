package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.platform.rendering.fm.adapters.DocumentTemplate;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

import edu.emory.mathcs.backport.java.util.Collections;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class BreadcrumbsArrayTemplateMethod implements TemplateMethodModelEx {

    private static final Log LOG = LogFactory.getLog(BreadcrumbsArrayTemplateMethod.class);

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException(
                    "BreadcrumbsArray takes only ONE parameter !");
        }
        DocumentTemplate a = (DocumentTemplate) arguments.get(0);
        if(a == null) {
            return new DocumentModelListImpl();
        }
        DocumentModel document = a.getDocument();
        DocumentModelList list = new DocumentModelListImpl(2);
        try {
            buildList(list, document);
        } catch (ClientException e) {
            LOG.error(e, e);
        }
        Collections.reverse(list);
        return list;
    }

    private void buildList(DocumentModelList list, final DocumentModel doc) throws ClientException {
        list.add(doc);
        if (LabsSiteConstants.Docs.SITE.type().equals(doc.getType())) {
            return;
        }
        buildList(list, doc.getCoreSession().getDocument(doc.getParentRef()));
    }

}
