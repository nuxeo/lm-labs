package com.leroymerlin.corp.fr.nuxeo.labs.site;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.local.LocalSession;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.common.core.security.SecurityDataHelper;


@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD, init=DefaultRepositoryInit.class, user = "system")
public class SiteManagerTest {
    @Inject
    SiteManager sm;

    @Inject
    CoreSession session;
    
    @Inject
    protected FeaturesRunner featuresRunner;


    @Test
    public void smIsNotNull() throws Exception {
        assertThat(sm,is(notNullValue()));
    }


    @Test
    public void canCreateAndGetASite() throws Exception {
        LabsSite site = sm.createSite(session, "Mon titre", "myurl");
        assertThat(site,is(notNullValue()));
        site.setDescription("Un super site");
        session.saveDocument(site.getDocument());
        session.save();

        site = sm.getSite(session, "myurl");
        assertThat(site, is(notNullValue()));
        assertThat(site.getDescription(), is("Un super site"));
        assertThat(site.getTitle(), is("Mon titre"));

    }


    @Test(expected=SiteManagerException.class)
    public void cantCreateTwoSitesWithSameUrl() throws Exception {
        LabsSite site = sm.createSite(session, "Mon titre", "myurl");
        assertThat(site,is(notNullValue()));
        session.save();

        sm.createSite(session, "Mon titre", "myurl");
    }

    @Test
    public void cantCreateASiteWithoutEitherTitleOrUrl() throws ClientException  {
        try {
            sm.createSite(session, "", "toto");
            fail("May not be able to create a site without title");
        } catch (SiteManagerException e) {
            //This is ok
        }

        try {
            sm.createSite(session, "", "");
            fail("May not be able to create a site without url");
        } catch (SiteManagerException e) {
            //This is ok
        }

        try {
            sm.createSite(session, "toto", "");
            fail("May not be able to create a site without title and url");
        } catch (SiteManagerException e) {
            //This is ok
        }
    }
    @Test(expected=SiteManagerException.class)
    public void cantGetNonExistentSite() throws Exception {
        sm.getSite(session, "nonexistenturl");
    }

    @Test
    public void canRemoveSite() throws Exception {
        LabsSite site = sm.createSite(session, "Mon titre", "myurl");
        session.save();
        assertThat(sm.getSite(session, "myurl"),is(notNullValue()));
        sm.removeSite(session, site);
        assertThat(sm.siteExists(session,"myurl"), is(false));


    }

    @Test
    public void canRetrieveAllSites() throws Exception {
        sm.createSite(session, "Mon titre", "myurl");
        session.save();
        assertThat(sm.getAllSites(session).size(),is(1));
        sm.createSite(session, "Mon titre2", "myurl2");
        session.save();
        assertThat(sm.getAllSites(session).size(),is(2));

    }


    @Test
    public void iCanUpdateASite() throws Exception {
        LabsSite site = sm.createSite(session, "Mon titre", "myurl");
        site.setDescription("desc");
        session.save();

        site = sm.getSite(session, "myurl");
        site.setTitle("Mon titre 2");
        site.setDescription("desc2");
        sm.updateSite(session,site);
        session.save();

        site = sm.getSite(session, "myurl");
        assertThat(site.getTitle(),is("Mon titre 2"));
        assertThat(site.getDescription(), is("desc2"));
        assertThat(site.getURL(), is("myurl"));


        site.setURL("myurl2");
        sm.updateSite(session,site);
        session.save();

        site = sm.getSite(session, "myurl2");
        assertThat(site.getURL(), is("myurl2"));



    }

    @Test
    public void iCanUpdateJustTheTitle() throws Exception {

    }

    @Test(expected=SiteManagerException.class)
    public void iCantUpdateASiteWithAnUrlOfAnotherSite() throws Exception {
        sm.createSite(session, "Mon titre", "myurl");
        LabsSite site = sm.createSite(session, "Mon titre", "myurl2");

        session.save();

        site.setURL("myurl");
        sm.updateSite(session, site);
        session.save();

    }
    
    @Test
    public void iCreateSiteWithGoodsRights() throws Exception{
        CoreSession sess = changeUser("CGM");
        LabsSite site = sm.createSite(sess, "Mon titre", "myurl");
        site.setDescription("desc");
        sess.save();
        SecurityData data = SecurityDataHelper.buildSecurityData(site.getDocument());
        
        assertThat(data.getCurrentDocGrant().size(), is(2));
        List<String> CGM = data.getCurrentDocGrant().get("CGM");
        assertThat(CGM, notNullValue());
        assertThat(CGM.size(), is(1));
        assertThat(CGM.get(0), is("Everything"));
        
        List<String> administrators = data.getCurrentDocGrant().get("administrators");
        assertThat(administrators, notNullValue());
        assertThat(administrators.size(), is(1));
        assertThat(administrators.get(0), is("Everything"));
    }
    
    @Test
    public void iCanGetAdministrators() throws Exception{
        CoreSession sess = changeUser("CGM");
        LabsSite site = sm.createSite(sess, "Mon titre", "myurl");
        site.setDescription("desc");
        sess.save();
        
        List<String> administrators = site.getAdministratorsSite();
        
        assertThat(administrators, notNullValue());
        assertThat(administrators.size(), is(1));
        assertThat(administrators.get(0), is("CGM"));
    }
    
    
    private CoreSession changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        Map<String, Serializable> ctx = new HashMap<String, Serializable>();
        ctx.put("username", username);
        CoreSession userSession = LocalSession.createInstance();
        userSession.connect(coreFeature.getRepository().getName(), ctx);
        return userSession;
    }


}
