package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

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
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy({
    "org.nuxeo.ecm.platform.picture.api",
    "org.nuxeo.ecm.platform.picture.convert",
    "org.nuxeo.ecm.platform.filemanager.core.listener",
    "org.nuxeo.ecm.platform.commandline.executor"
})
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
    public void canGetTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        assertThat(theme,is(notNullValue()));
        assertThat(theme.getName(),is("labs"));
    }

    @Test
    public void cantSetAndGetBannerFromTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        theme.setBanner(getTestBlob());
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        Blob blob = theme.getBanner();
        assertThat(blob,is(notNullValue()));
        assertThat(blob.getLength() > 0 , is(true));

    }

    @Test
    public void cantSetAndGetLogoFromTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        theme.setLogo(getTestBlob());
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        Blob blob = theme.getLogo();
        assertThat(blob,is(notNullValue()));
        assertThat(blob.getLength() > 0 , is(true));
    }

    @Test
    public void cantSetAndGetResizedLogoFromTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        theme.setLogo(getTestBlob());
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        Blob blob = theme.getLogo();
        assertThat(blob,is(notNullValue()));
        assertThat(blob.getLength() > 0 , is(true));
        
        int width = theme.getLogoWidth();
        tm = site.getThemeManager();
        theme = tm.getTheme();
        theme.setLogoResizeRatio(50);
        session.saveDocument(theme.getDocument());
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        assertThat(theme.getLogoWidth() , is(width/2));
    }

    @Test
    public void cantSetAndGetLogoParametersFromTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        assertThat(theme.getLogoPosX() , is(0));
        assertThat(theme.getLogoPosY() , is(0));
        assertThat(theme.getLogoResizeRatio() , is(100));
        theme.setLogoPosX(1);
        theme.setLogoPosY(2);
        theme.setLogoResizeRatio(50);
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        assertThat(theme.getLogoPosX() , is(1));
        assertThat(theme.getLogoPosY() , is(2));
        assertThat(theme.getLogoResizeRatio() , is(50));
    }

    @Test
    public void canGetThemeByName() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        theme.setBanner(getTestBlob());
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme("labs");

        assertThat(theme,is(notNullValue()));
        Blob blob = theme.getBanner();
        assertThat(blob.getLength() > 0 , is(true));

    }
    
    @Test
    public void canSetTheme() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        tm = site.getThemeManager();
        SiteTheme theme = tm.getTheme();
        assertThat(theme,is(notNullValue()));
        assertThat(theme.getName(),is("supplyChain"));
    }
    
    @Test
    public void canSetStyle() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        theme.setStyle("style");
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        
        assertThat((String)theme.getDocument().getPropertyValue("sitetheme:style"),is("style"));
    }
    
    @Test
    public void canGetStyle() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        theme.setStyle("style");
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        
        assertThat(theme.getStyle(),is("style"));
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
