package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class StoryboardItem {

    public static final Log log = LogFactory.getLog(StoryboardItem.class);

    protected final DocumentModel doc;

    protected final int position;

    protected final String blobPropertyName;

    protected final String filename;

    protected String timecode = "0";

    public StoryboardItem(DocumentModel doc, String basePropertyPath,
            int position) {
        this.doc = doc;
        this.position = position;
        String propertyPath = basePropertyPath + "/" + position;
        blobPropertyName = propertyPath + "/content";
        filename = String.format("storyboard-%03d.jpeg", position);
        try {
            Double tc = doc.getProperty(propertyPath + "/timecode").getValue(
                    Double.class);
            if (tc != null) {
                timecode = String.format("%f", Math.floor(tc));
            }
            // TODO: read filename from blob too
        } catch (Exception e) {
            log.warn(e);
        }
    }

    public String getUrl() {
        if (doc == null) {
            return null;
        }
        String bigDownloadURL = "nxbigfile" + "/";
        bigDownloadURL += doc.getRepositoryName() + "/";
        bigDownloadURL += doc.getRef().toString() + "/";
        bigDownloadURL += blobPropertyName + "/";
        bigDownloadURL += filename;
        return bigDownloadURL;
    }

    public String getTimecode() {
        return timecode;
    }

}
