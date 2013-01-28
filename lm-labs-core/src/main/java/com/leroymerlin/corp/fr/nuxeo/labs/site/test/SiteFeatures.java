package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Deploy( {
    "org.nuxeo.ecm.platform.picture.core",
    "org.nuxeo.ecm.platform.picture.api",
    "org.nuxeo.ecm.core.api",
    "com.leroymerlin.labs.core"
})
@Features({CoreFeature.class,PlatformFeature.class})
public class SiteFeatures extends SimpleFeature {
    public static final String SITE_NAME = "monsite";
}
