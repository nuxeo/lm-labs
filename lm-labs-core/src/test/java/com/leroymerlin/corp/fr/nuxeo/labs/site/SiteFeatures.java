package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Deploy( {
    "org.nuxeo.ecm.platform.picture.core",
    "com.leroymerlin.labs.core"
})
@Features(PlatformFeature.class)
public class SiteFeatures extends SimpleFeature {
    public static final String SITE_NAME = "site1";
}
