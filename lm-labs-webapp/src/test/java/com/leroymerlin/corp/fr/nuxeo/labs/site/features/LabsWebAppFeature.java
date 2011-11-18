package com.leroymerlin.corp.fr.nuxeo.labs.site.features;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.webengine.jaxrs.session.SessionFactory;
import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.test.WorkingDirectoryConfigurator;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RuntimeFeature;
import org.nuxeo.runtime.test.runner.RuntimeHarness;
import org.nuxeo.runtime.test.runner.SimpleFeature;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

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
        "org.nuxeo.ecm.platform", "org.nuxeo.ecm.platform.types.api",
        "org.nuxeo.ecm.platform.types.core",
        "org.nuxeo.ecm.platform.mimetype.api",
        "org.nuxeo.ecm.platform.mimetype.core" })
public class LabsWebAppFeature extends SimpleFeature implements
        WorkingDirectoryConfigurator, FrameworkListener {

    @Override
    public void initialize(FeaturesRunner runner) throws Exception {
        RuntimeHarness harness = runner.getFeature(RuntimeFeature.class)
                .getHarness();
        harness.addWorkingDirectoryConfigurator(this);

    }

    public void configure(RuntimeHarness harness, File workingDir)
            throws IOException {
        SessionFactory.setDefaultRepository("test");
        File dest = new File(workingDir, "web/root.war/WEB-INF/");
        dest.mkdirs();

        InputStream in = getResource("web.xml").openStream();
        dest = new File(workingDir + "/web/root.war/WEB-INF/", "web.xml");

        try {
            FileUtils.copyToFile(in, dest);
        } finally {
            in.close();
        }

        copyConfigResource("webengine.properties", workingDir);
        copyConfigResource("opensocial.properties", workingDir);

        harness.getOSGiAdapter()
                .addFrameworkListener(this);
    }

    private void copyConfigResource(String name, File workingDir)
            throws IOException {
        File dest = new File(workingDir, "config");
        dest.mkdirs();
        InputStream in = getResource(name).openStream();
        dest = new File(workingDir + "/config/", name);

        try {
            FileUtils.copyToFile(in, dest);
        } finally {
            in.close();
        }

    }

    private static URL getResource(String resource) {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResource(resource);
    }

    @Override
    public void frameworkEvent(FrameworkEvent event) {
        try {
            if (event.getType() == FrameworkEvent.STARTED) {
//                Framework.getService(OpenSocialService.class)
//                        .setupOpenSocial();
//                ContextListenerDelayer.activate(event);
//                AuthenticationFilterDelayer.activate(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
