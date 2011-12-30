package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;

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
    
    @SuppressWarnings("unchecked")
    @Test
    public void canSetProperties() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        Map<String, ThemeProperty> properties = new HashMap<String, ThemeProperty>();
        ThemeProperty prop = new ThemeProperty("bgcolor", "bgcolor-value", "bgcolor-label", "bgcolor-description");
        properties.put("bgcolor", prop);
        prop = new ThemeProperty("background-url", "background-url-value", "background-url-label", "background-url-description");
        properties.put("background-url", prop);
        
        theme.setProperties(properties);
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        
        assertThat((List<Map<String, Object>>)theme.getDocument().getPropertyValue("sitetheme:properties"), notNullValue());
        assertThat(((List<Map<String, Object>>)theme.getDocument().getPropertyValue("sitetheme:properties")).size(), is(2));
        Map<String, Object> get0 = ((List<Map<String, Object>>)theme.getDocument().getPropertyValue("sitetheme:properties")).get(0);
        Map<String, Object> get1 = ((List<Map<String, Object>>)theme.getDocument().getPropertyValue("sitetheme:properties")).get(1);
        if(get0.get("key").equals("bgcolor")){
            assertThat(get0.get("value").toString(), is("bgcolor-value"));
            assertThat(get0.get("label").toString(), is("bgcolor-label"));
            assertThat(get0.get("description").toString(), is("bgcolor-description"));
        }
        else if(get0.get("key").equals("background-url")){
            assertThat(get0.get("value").toString(), is("background-url-value"));
            assertThat(get0.get("label").toString(), is("background-url-label"));
            assertThat(get0.get("description").toString(), is("background-url-description"));
        }
        else{
            assertThat(null, notNullValue());
        }
        
        if(get1.get("key").equals("bgcolor")){
            assertThat(get1.get("value").toString(), is("bgcolor-value"));
            assertThat(get1.get("label").toString(), is("bgcolor-label"));
            assertThat(get1.get("description").toString(), is("bgcolor-description"));
        }
        else if(get1.get("key").equals("background-url")){
            assertThat(get1.get("value").toString(), is("background-url-value"));
            assertThat(get1.get("label").toString(), is("background-url-label"));
            assertThat(get1.get("description").toString(), is("background-url-description"));
        }
        else{
            assertThat(null, notNullValue());
        }
    }
    
    @Test
    public void canGetProperties() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        Map<String, ThemeProperty> properties = new HashMap<String, ThemeProperty>();
        ThemeProperty prop = new ThemeProperty("bgcolor", "bgcolor-value", "bgcolor-label", "bgcolor-description");
        properties.put("bgcolor", prop);
        prop = new ThemeProperty("background-url", "background-url-value", "background-url-label", "background-url-description");
        properties.put("background-url", prop);
        
        theme.setProperties(properties);
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        properties = theme.getProperties();

        assertThat(properties.get("bgcolor").getValue(), is("bgcolor-value"));
        assertThat(properties.get("bgcolor").getLabel(), is("bgcolor-label"));
        assertThat(properties.get("bgcolor").getDescription(), is("bgcolor-description"));
        assertThat(properties.get("background-url").getValue(), is("background-url-value"));
        assertThat(properties.get("background-url").getLabel(), is("background-url-label"));
        assertThat(properties.get("background-url").getDescription(), is("background-url-description"));
    }
    
    @Test
    public void canSetLastRead() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        theme.setLastRead(5000l);
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();

        assertThat(theme.getDocument().getPropertyValue("sitetheme:lastRead"), notNullValue());
        assertThat(((Long)theme.getDocument().getPropertyValue("sitetheme:lastRead")).longValue(), is(5000l));
    }
    
    @Test
    public void canGetDefaultLastRead() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();

        assertThat(theme.getLastRead(), is(0l));
    }
    
    @Test
    public void canGetLastRead() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        theme.setLastRead(5000l);
        
        session.saveDocument(theme.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();

        assertThat(theme.getLastRead(), is(5000l));
    }
    
    @Test
    public void canVerifyFirstToLoadWithNoFile() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        ThemePropertiesManage tpm = new ThemePropertiesManage(theme.getProperties());

        assertThat(tpm.isLoaded("themeProperties/propertiesyy", theme.getLastRead()), is(true));
    }
    
    @Test
    public void canVerifyFirstToLoad() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        ThemePropertiesManage tpm = new ThemePropertiesManage(theme.getProperties());

        String pathProperties = FileUtils.getResourcePathFromContext("themeProperties/properties");
        assertThat(tpm.isLoaded(pathProperties, theme.getLastRead()), is(false));
    }
    
    @Test
    public void canVerifyNoToLoad() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        String pathProperties = FileUtils.getResourcePathFromContext("themeProperties/properties");
        File testFile = new File(pathProperties);
        
        theme.setLastRead(testFile.lastModified() + 100000);
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        ThemePropertiesManage tpm = new ThemePropertiesManage(theme.getProperties());
        
        assertThat(tpm.isLoaded(pathProperties, theme.getLastRead()), is(true));
    }
    
    @Test
    public void canLoad() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        
        String pathProperties = FileUtils.getResourcePathFromContext("themeProperties/properties");
        File testFile = new File(pathProperties);
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        Map<String, ThemeProperty> properties = theme.getProperties();
        ThemePropertiesManage tpm = new ThemePropertiesManage(properties);
        tpm.loadProperties(new FileInputStream(testFile));
        properties = tpm.getProperties();

        assertThat(properties.size(), is(5));
        assertThat(properties.get("@basefont"), notNullValue());
        assertThat(properties.get("@basefont").getLabel(), is("Police du site"));
        assertThat(properties.get("@basefont").getDescription(), is("Séparé par ',' et délimité par '\"'"));
    }
    
    @Test
    public void canAddPropertyToLoadInMapWithASameKey() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        Map<String, ThemeProperty> properties = new HashMap<String, ThemeProperty>();
        properties.put("@baseFontColorTitle", new ThemeProperty("@baseFontColorTitle", null, null, null));
        theme.setProperties(properties);
        
        String pathProperties = FileUtils.getResourcePathFromContext("themeProperties/properties");
        File testFile = new File(pathProperties);
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        properties = theme.getProperties();
        ThemePropertiesManage tpm = new ThemePropertiesManage(properties);
        tpm.loadProperties(new FileInputStream(testFile));
        properties = tpm.getProperties();

        assertThat(properties.size(), is(5));
    }
    
    @Test
    public void canAddPropertyToLoadInMapWithAdifferentKey() throws Exception {
        SiteThemeManager tm = site.getThemeManager();
        tm.setTheme("supplyChain");
        SiteTheme theme = tm.getTheme();
        Map<String, ThemeProperty> properties = new HashMap<String, ThemeProperty>();
        properties.put("@baseFontColorTitleyy", new ThemeProperty("@baseFontColorTitleyy", null, null, null));
        theme.setProperties(properties);
        
        String pathProperties = FileUtils.getResourcePathFromContext("themeProperties/properties");
        File testFile = new File(pathProperties);
        
        session.saveDocument(theme.getDocument());
        session.save();
        
        site = sm.getSite(session, "myurl");
        tm = site.getThemeManager();
        theme = tm.getTheme();
        properties = theme.getProperties();
        ThemePropertiesManage tpm = new ThemePropertiesManage(properties);
        tpm.loadProperties(new FileInputStream(testFile));
        properties = tpm.getProperties();

        assertThat(properties.size(), is(6));
    }
}
