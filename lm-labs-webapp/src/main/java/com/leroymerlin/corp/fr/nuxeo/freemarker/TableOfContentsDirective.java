package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class TableOfContentsDirective implements TemplateDirectiveModel {
    
    private static final String PARAM_NAME_TAG = "tocTag";
    private static final String PARAM_NAME_UL_CLASS = "ulClass";
    private static final String PARAM_NAME_SELECTOR = "anchorSelector";
    private static final String PARAM_NAME_NO_REPLACE_CLASS = "noReplaceClass";
    
    private static final List<String> paramKeys = new ArrayList<String>(
            Arrays.asList(
                    PARAM_NAME_TAG,
                    PARAM_NAME_UL_CLASS,
                    PARAM_NAME_SELECTOR,
                    PARAM_NAME_NO_REPLACE_CLASS
                    ));


    private static final Log LOG = LogFactory.getLog(TableOfContentsDirective.class);

    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String logPrefix = "<execute> ";
        if (loopVars.length != 0) {
                throw new TemplateModelException(
                    "This directive doesn't allow loop variables.");
        }
        
        // If there is non-empty nested content:
        if (body != null) {
            String html = renderBody(body);
            LOG.debug(logPrefix + html);
            TableOfContentsGenerator generator = new TableOfContentsGenerator.Builder(html)
            .noReplaceParentClass(getParamStringValue(params, PARAM_NAME_NO_REPLACE_CLASS))
            .selector(getParamStringValue(params, PARAM_NAME_SELECTOR))
            .tag(getParamStringValue(params, PARAM_NAME_TAG))
            .ulClass(getParamStringValue(params, PARAM_NAME_UL_CLASS))
            .build();
            Writer writer = env.getOut();
            writer.write(generator.getHtml());
        } else {
            throw new RuntimeException("missing body");
        }

    }

    public String getParamStringValue(@SuppressWarnings("rawtypes") final Map params, final String paramKey) {
        if (params.isEmpty()) {
            return null;
        }
        if (!paramKeys.contains(paramKey)) {
            LOG.warn("This directive doesn't allow parameter '" + paramKey + "'.");
            return null;
        } else {
            SimpleScalar simpleScalar = (SimpleScalar) params.get(paramKey);
            if (simpleScalar == null) {
                LOG.warn("Vale of parameter '" + paramKey + "' is null.");
                return null;
            }
            return simpleScalar.getAsString();
        }
    }
    
    private String renderBody(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        Writer sw = new StringWriter();
        body.render(sw);
        return sw.toString();
    }
}
