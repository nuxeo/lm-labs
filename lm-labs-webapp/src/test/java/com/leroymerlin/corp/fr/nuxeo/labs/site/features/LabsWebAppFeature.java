package com.leroymerlin.corp.fr.nuxeo.labs.site.features;

import javax.sql.DataSource;

import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Features({ WebEngineFeature.class })
@Deploy({
    "com.leroymerlin.labs.webapp",
    "com.leroymerlin.labs.core",
    "org.nuxeo.ecm.platform.picture.core",
    "org.nuxeo.ecm.platform.picture.api",
        "org.nuxeo.ecm.platform.picture.convert",
        "org.nuxeo.ecm.platform.filemanager.api",
        "org.nuxeo.ecm.platform.filemanager.core",
        "org.nuxeo.ecm.platform.filemanager.core.listener",
        "org.nuxeo.ecm.platform.commandline.executor",
        "org.nuxeo.ecm.platform.query.api",
        "org.nuxeo.ecm.core.persistence",
        "org.nuxeo.ecm.platform"
//        "com.leroymerlin.corp.fr.nuxeo.portal.picturebook.webapp.test:hibernate-contrib.xml",
//        "org.nuxeo.ecm.platform.rating.api",
//        "org.nuxeo.ecm.platform.rating.core",
//        "com.leroymerlin.corp.fr.nuxeo.portal.common.webapp",
//        "org.nuxeo.ecm.directory.api",
//        "org.nuxeo.ecm.directory",
//        "org.nuxeo.ecm.directory.sql",
//        "com.leroymerlin.corp.fr.nuxeo.portal.picturebook.webapp.test:default-sql-directories.xml"
        })
public class LabsWebAppFeature extends SimpleFeature {

    private static DataSource dataSource = null;

    @Override
    public void initialize(FeaturesRunner runner) {

//        if (dataSource == null) {
//            NamingContextFactory.setAsInitial();
//
//            dataSource = PlatformFeature.createDataSource("jdbc:hsqldb:mem:directories");
//            try {
//                InitialContext initialCtx = new InitialContext();
//                String dsName = DataSourceHelper.getDataSourceJNDIName("nxsqldirectory");
//                try {
//                    initialCtx.lookup(dsName);
//                } catch (NameNotFoundException e) {
//                    initialCtx.bind(dsName, dataSource);
//                }
//            } catch (NamingException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
