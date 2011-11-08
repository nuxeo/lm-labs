package com.leroymerlin.corp.fr.nuxeo.labs.site.page;
import static com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit.FOLDER1_NAME;
import static com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit.PAGE_CLASSEUR_TITLE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
public class PagePathTests {

    @Inject
    CoreSession session;

    @Inject
    SiteManager sm;

    LabsSite site = null;

    private PageClasseur classeur;

    @Before
    public void doBefore() throws Exception {
        site = sm.getSite(session, OfmRepositoryInit.SITE_URL);
        classeur = session.getDocument(
                new PathRef(site.getDocument()
                        .getPathAsString() + "/tree/" + PAGE_CLASSEUR_TITLE))
                .getAdapter(PageClasseur.class);
    }

    @Test
    public void canGetThePathOfAPage() throws Exception {

        assertThat(classeur.getPath(), is("ofm/"+ PAGE_CLASSEUR_TITLE));

    }

    @Test
    public void canGetThePathOfADocument() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef(classeur.getDocument().getPathAsString() + "/" + FOLDER1_NAME ));
        SiteDocument sd = folder.getAdapter(SiteDocument.class);

        assertThat(sd.getParentPagePath(), is("ofm/" + PAGE_CLASSEUR_TITLE));
        assertThat(sd.getResourcePath(), is("ofm/" + PAGE_CLASSEUR_TITLE + "/" + FOLDER1_NAME ) );

    }

}
