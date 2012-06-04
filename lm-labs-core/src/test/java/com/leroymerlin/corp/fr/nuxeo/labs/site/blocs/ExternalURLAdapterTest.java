package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@RunWith(FeaturesRunner.class)
@Features(com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
public class ExternalURLAdapterTest {

    private static final String URL = "URL";

    private static final String NAME = "name";

    private static final String EXTERNAL_URL_TYPE = LabsSiteConstants.Docs.EXTERNAL_URL.type();

    @Inject
    private CoreSession session;

    @Test
    public void iCanCreateANewsDocument() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "externalURL", EXTERNAL_URL_TYPE);

        // Persist document in db
        doc = session.createDocument(doc);

        // Commit
        session.save();

        doc = session.getDocument(new PathRef("/externalURL"));
        assertThat(doc, is(notNullValue()));

    }

    @Test
    public void iCanCreateAExternalURLAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myExternalURL",EXTERNAL_URL_TYPE);
        
        ExternalURL news = Tools.getAdapter(ExternalURL.class, doc, session);
        assertThat(news,is(notNullValue()));
        
        //Persist document in db
        doc = session.createDocument(doc);
        
        //Commit
        session.save();
        
        doc = session.getDocument(new PathRef("/myExternalURL"));
        news = Tools.getAdapter(ExternalURL.class, doc, session);
        assertThat(news,is(notNullValue()));

    }

    @Test
    public void iCanGetPropertiesOnNewsAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myExternalURL",EXTERNAL_URL_TYPE);

        doc.setPropertyValue("dc:creator", "creator");
        
        ExternalURL ext_url = Tools.getAdapter(ExternalURL.class, doc, session);
        assertThat(ext_url,is(notNullValue()));
        ext_url.setName(NAME);
        ext_url.setURL(URL);
        
        //Persist document in db
        doc = session.createDocument(doc);
        
        //Commit
        session.save();
        
        doc = session.getDocument(new PathRef("/myExternalURL"));
        ext_url = Tools.getAdapter(ExternalURL.class, doc, session);
        assertThat(ext_url,is(notNullValue()));
        assertThat(ext_url.getName(), is(NAME));
        assertThat(ext_url.getURL(), is("http://" + URL));

    }
}
