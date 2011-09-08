package com.leroymerlin.corp.fr.nuxeo.freemarker;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UserFullNameTemplateMethod implements TemplateMethodModelEx {

    private static final Log LOG = LogFactory.getLog(UserFullNameTemplateMethod.class);

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException(
                    "UserFullNameTemplateMethod takes only ONE parameter !");
        }
        String id = ((SimpleScalar) arguments.get(0)).getAsString();
        String displayName = id;
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            if (userManager.getPrincipal(id) != null) {
                if (!StringUtils.isEmpty(userManager.getPrincipal(id)
                        .getFirstName())
                        || !StringUtils.isEmpty(userManager.getPrincipal(id)
                                .getLastName())) {
                    displayName = userManager.getPrincipal(id)
                            .getFirstName() + " "
                            + userManager.getPrincipal(id)
                            .getLastName();
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return displayName;
    }

}
