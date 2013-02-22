package com.leroymerlin.common.freemarker;

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
        SimpleScalar firstArg = (SimpleScalar) arguments.get(0);
        if (firstArg == null) {
            throw new TemplateModelException(
                    "UserFullNameTemplateMethod does not accept null as a parameter !");
        }
        String id = firstArg.getAsString();
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
