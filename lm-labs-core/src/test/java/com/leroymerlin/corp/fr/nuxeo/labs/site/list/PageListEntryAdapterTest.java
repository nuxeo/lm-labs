package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class PageListEntryAdapterTest {
    private static final boolean CHECKBOX = true;
    private static final Calendar CAL = Calendar.getInstance();
    private static final String TEXT = "text";
    private static final int ID_HEADER = 1;
    private static final String PATH_SEPARATOR = "/";
    private static final String DATA_TITLE = "data";
    @Inject
    private CoreSession session;

    @Test
    public void canCreateDataModel() throws Exception {
        new PageListEntryAdapter.Model(session, PATH_SEPARATOR, DATA_TITLE).create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + DATA_TITLE)));
    }

    @Test
    public void canCreateHeaderList() throws Exception {
        PageListEntryAdapter.Model model = new PageListEntryAdapter.Model(session, PATH_SEPARATOR, DATA_TITLE);
        PageListEntry entry = model.getAdapter();
        assertThat(entry,is(notNullValue()));
        
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setDataURL(url);
        entry = model.create();
        
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + DATA_TITLE)));
        assertTrue(ID_HEADER == entry.getIdHeader());
        assertTrue(TEXT.equals(entry.getText()));
        assertTrue(CAL.equals(entry.getDate()));
        assertTrue(CHECKBOX == entry.getCheckBox());
        assertTrue(url.equals(entry.getDataURL()));
    }

}
