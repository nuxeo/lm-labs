package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.nuxeo.common.jndi.NamingContextFactory;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.api.DataSourceHelper;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Features( { WebEngineFeature.class })
@Deploy( {
        "com.leroymerlin.labs.webapp",
        "org.nuxeo.ecm.platform.picture.core",
        "org.nuxeo.ecm.platform.picture.api",
        "org.nuxeo.ecm.platform.picture.convert",
        "org.nuxeo.ecm.platform.filemanager.core.listener",
        "org.nuxeo.ecm.platform.commandline.executor",
        "org.nuxeo.ecm.core.persistence",
        "org.nuxeo.ecm.platform",
        "com.leroymerlin.labs.webapp.test:OSGI-INF/hibernate-contrib.xml",
        "org.nuxeo.ecm.platform.comment.api",
        "org.nuxeo.ecm.platform.comment.core",
        "org.nuxeo.ecm.platform.comment:OSGI-INF/comment-listener-contrib.xml",
        "org.nuxeo.ecm.platform.comment:OSGI-INF/CommentService.xml",
        "org.nuxeo.ecm.relations.jena",
        "org.nuxeo.ecm.relations.api",
        "org.nuxeo.ecm.relations",
        "org.nuxeo.ecm.platform.rating.api",
        "org.nuxeo.ecm.platform.rating.core",

        "org.nuxeo.ecm.platform.rating.core",
        "org.nuxeo.ecm.core.persistence",
        "org.nuxeo.ecm.platform.rating.api",
        "org.nuxeo.ecm.platform.tag",
        "org.nuxeo.ecm.platform.content.template",
        "org.nuxeo.ecm.platform.filemanager.api",
        "org.nuxeo.ecm.platform.filemanager.core",
        "org.nuxeo.ecm.platform.types.core",
        "org.nuxeo.ecm.platform.types.api",
        "com.leroymerlin.labs.core",
        "org.nuxeo.ecm.directory.api",
        "org.nuxeo.ecm.directory",
        "com.leroymerlin.corp.fr.nuxeo.view.service",
        "org.nuxeo.ecm.directory.sql",
        "com.leroymerlin.labs.core.test:OSGI-INF/hibernate-contrib.xml" })
public class MyJettyFeature extends SimpleFeature {

    private static DataSource dataSource = null;

    @Override
    public void initialize(FeaturesRunner runner) {

        if (dataSource == null) {
            NamingContextFactory.setAsInitial();

            dataSource = PlatformFeature.createDataSource("jdbc:hsqldb:mem:directories");
            try {
                InitialContext initialCtx = new InitialContext();
                String dsName = DataSourceHelper.getDataSourceJNDIName("nxsqldirectory");
                try {
                    initialCtx.lookup(dsName);
                } catch (NameNotFoundException e) {
                    initialCtx.bind(dsName, dataSource);
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
}
