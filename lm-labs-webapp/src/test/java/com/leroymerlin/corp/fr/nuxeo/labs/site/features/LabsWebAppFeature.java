package com.leroymerlin.corp.fr.nuxeo.labs.site.features;

import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Features({ WebEngineFeature.class })
@Deploy({ "com.leroymerlin.labs.webapp", "com.leroymerlin.labs.core",
        "org.nuxeo.ecm.platform.picture.core",
        "org.nuxeo.ecm.platform.picture.api",
        "org.nuxeo.ecm.platform.picture.convert",
        "org.nuxeo.ecm.platform.filemanager.api",
        "org.nuxeo.ecm.platform.filemanager.core",
        "org.nuxeo.ecm.platform.filemanager.core.listener",
        "org.nuxeo.ecm.platform.commandline.executor",
        "org.nuxeo.ecm.platform.query.api", "org.nuxeo.ecm.core.persistence",
        "org.nuxeo.ecm.platform",
        "org.nuxeo.ecm.platform.types.api",
        "org.nuxeo.ecm.platform.types.core",
        "org.nuxeo.ecm.platform.mimetype.api",
        "org.nuxeo.ecm.platform.mimetype.core",
        "org.nuxeo.ecm.opensocial.spaces"
})
public class LabsWebAppFeature extends SimpleFeature {


    @Override
    public void initialize(FeaturesRunner runner) {

    }

}
