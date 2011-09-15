package com.leroymerlin.corp.fr.nuxeo.labs.site;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;


@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD, init=DefaultRepositoryInit.class)
public class SiteManagerTest {
    @Inject
    SiteManager sm;

    @Inject
    CoreSession session;


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
}
