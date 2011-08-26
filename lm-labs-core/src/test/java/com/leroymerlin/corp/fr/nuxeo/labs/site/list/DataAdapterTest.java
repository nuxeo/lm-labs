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
public class DataAdapterTest {
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
        new DataAdapter.Model(session, PATH_SEPARATOR, DATA_TITLE).create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + DATA_TITLE)));
    }

    @Test
    public void canCreateHeaderList() throws Exception {
        DataAdapter.Model model = new DataAdapter.Model(session, PATH_SEPARATOR, DATA_TITLE);
        Data data = model.getAdapter();
        assertThat(data,is(notNullValue()));
        
        data.setIdHeader(ID_HEADER);
        data.setText(TEXT);
        data.setDate(CAL);
        data.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        data.setDataURL(url);
        data = model.create();
        
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + DATA_TITLE)));
        assertTrue(ID_HEADER == data.getIdHeader());
        assertTrue(TEXT.equals(data.getText()));
        assertTrue(CAL.equals(data.getDate()));
        assertTrue(CHECKBOX == data.getCheckBox());
        assertTrue(url.equals(data.getDataURL()));
    }

}
