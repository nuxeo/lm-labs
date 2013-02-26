package com.leroymerlin.common.core.adapter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.common.core.utils.BlobUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy( {
        "com.leroymerlin.labs.core.test:OSGI-INF/test-core-types-contrib.xml" })
public class FilesAdapterTest {

    private DocumentModel doc;

    @Inject
    public FilesAdapterTest(CoreSession session) throws ClientException {
        doc = session.createDocumentModel("/", "pb", "DocumentWithFiles");
        doc = session.createDocument(doc);
        session.save();
    }

    @Test
    public void iCanGetFilesAdapter() {
        FilesAdapter adapter = doc.getAdapter(FilesAdapter.class);
        assertNotNull(doc);
        assertNotNull(adapter);
    }

    @Test
    public void iCanAddFile() throws ClientException {
        FilesAdapter adapter = doc.getAdapter(FilesAdapter.class);
        FileBlob file = BlobUtils.createBlob("images/header_house.png",
                "header_house.png", "image/jpg");
        adapter.addFile(file, "test1");
        assertNull(adapter.getFile("test2"));
        assertNotNull(adapter.getFile("test1"));
    }

    @Test
    public void iCanAddFiles() throws ClientException {
        FilesAdapter adapter = doc.getAdapter(FilesAdapter.class);
        FileBlob file = BlobUtils.createBlob("images/header_house.png",
                "header_house.png", "image/jpg");
        adapter.addFile(file, "test1");
        adapter.addFile(file, "test2");
        adapter.addFile(file, "test3");
        assertNotNull(adapter.getFile("test3"));
        assertNotNull(adapter.getFile("test1"));
        assertNotNull(adapter.getFile("test2"));

    }

    @Test
    public void iCanRemoveFile() throws ClientException {
        FilesAdapter adapter = doc.getAdapter(FilesAdapter.class);
        FileBlob file = BlobUtils.createBlob("images/header_house.png",
                "header_house.png", "image/jpg");
        adapter.addFile(file, "test1");
        assertNotNull(adapter.getFile("test1"));
        adapter.removeFile("test1");
        assertNull(adapter.getFile("test1"));
    }

}
