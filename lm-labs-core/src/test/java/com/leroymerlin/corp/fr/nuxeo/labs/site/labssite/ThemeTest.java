package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD, init=DefaultRepositoryInit.class)
public class ThemeTest {
    @Inject
    SiteManager sm;

    @Inject
    CoreSession session;

    private LabsSite site;


    @Before
    public void doBefore() throws Exception {

        site = sm.createSite(session, "Mon titre", "myurl");
        assertThat(site,is(notNullValue()));
        site.setDescription("Un super site");
        session.saveDocument(site.getDocument());
        session.save();
    }


    @Test
    public void canGetSiteTheme() throws Exception {
        SiteThemeManager tm = site.getSiteThemeManager();
        SiteTheme theme = tm.getTheme();
        assertThat(theme,is(notNullValue()));
        assertThat(theme.getName(),is("default"));
    }

    @Test
    public void cantSetAndGetBannerFromTheme() throws Exception {
        SiteThemeManager tm = site.getSiteThemeManager();
        SiteTheme theme = tm.getTheme();
        theme.setBanner(getTestBlob());
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getSiteThemeManager();
        theme = tm.getTheme();
        Blob blob = theme.getBanner();
        assertThat(blob,is(notNullValue()));
        assertThat(blob.getLength() > 0 , is(true));

    }



    private Blob getTestBlob() {
        String filename = "vision.jpg";
        File testFile = new File(
                FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setMimeType("image/jpeg");
        blob.setFilename(filename);
        return blob;
    }
}
