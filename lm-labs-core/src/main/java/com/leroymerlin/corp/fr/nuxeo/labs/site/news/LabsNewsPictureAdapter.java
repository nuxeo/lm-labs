package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_BY_LINE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CAPTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CREDIT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_DATE_CREATED;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_DESCRIPTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_HEADLINE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_HEIGHT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_LANGUAGE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_OBJECT_NAME;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_SOURCE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_WIDTH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.picture.api.ImageInfo;
import org.nuxeo.ecm.platform.picture.api.adapters.DefaultPictureAdapter;

public class LabsNewsPictureAdapter extends DefaultPictureAdapter {

    @Override
    protected void clearViews() throws ClientException {
        List<Map<String, Object>> viewsList = new ArrayList<Map<String, Object>>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> views = (List<Map<String, Object>>)doc.getProperty(VIEWS_PROPERTY).getValue();
        for (Map<String, Object> property : views) {
            if (isNotForSummary(property)) {
                viewsList.add(property);
            }
        }
        doc.getProperty(VIEWS_PROPERTY).setValue(viewsList);
    }
    
    private boolean isNotForSummary(Map<String, Object> property) throws PropertyException{
        boolean result = ((String)property.get("title")).equals("Original");
        result = result || ((String)property.get("title")).equals("OriginalJpeg");
        result = result || ((String)property.get("title")).equals("summary_truncated_picture");
        return !result;
    }

    @Override
    protected void addViews(List<Map<String, Object>> pictureTemplates,
            String filename, String title) throws IOException, ClientException {
        doc.setProperty("dublincore", "title", title);
        if (pictureTemplates != null) {
            // Use PictureBook Properties
            for (Map<String, Object> view : pictureTemplates) {
                Integer maxsize;
                if (view.get("maxsize") == null) {
                    maxsize = MEDIUM_SIZE;
                } else {
                    maxsize = ((Long) view.get("maxsize")).intValue();
                }
                createPictureimpl((String) view.get("description"),
                        (String) view.get("tag"), (String) view.get("title"),
                        maxsize, filename, width, height, depth, fileContent);
            }
//            createPictureimpl("truncated image",
//                    "img", "truncatedImage",
//                    5000, filename, width/2, height/2, depth/2, fileContent);
        } else {
            // Default properties When PictureBook doesn't exist
            createPictureimpl("Medium Size", "medium", "Medium", MEDIUM_SIZE,
                    filename, width, height, depth, fileContent);
            createPictureimpl(description, "original", "Original", null,
                    filename, width, height, depth, fileContent);
            createPictureimpl("Thumbnail Size", "thumb", "Thumbnail",
                    THUMB_SIZE, filename, width, height, depth, fileContent);
            createPictureimpl("Original Picture in JPEG format", "originalJpeg", "OriginalJpeg", null,
                    filename, width, height, depth, fileContent);

        }
    }

    @Override
    protected void setMetadata() throws IOException, ClientException {
        boolean imageInfoUsed = false;
        ImageInfo imageInfo = getImagingService().getImageInfo(fileContent);
        if (imageInfo != null) {
            width = imageInfo.getWidth();
            height = imageInfo.getHeight();
            depth = imageInfo.getDepth();
            imageInfoUsed = true;
        }
        Map<String, Object> metadata = getImagingService().getImageMetadata(
                fileContent);
        description = (String) metadata.get(META_DESCRIPTION);
        if (!imageInfoUsed) {
            width = (Integer) metadata.get(META_WIDTH);
            height = (Integer) metadata.get(META_HEIGHT);
        }
        doc.setPropertyValue("picture:" + FIELD_BYLINE,
                (String) metadata.get(META_BY_LINE));
        doc.setPropertyValue("picture:" + FIELD_CAPTION,
                (String) metadata.get(META_CAPTION));
        doc.setPropertyValue("picture:" + FIELD_CREDIT,
                (String) metadata.get(META_CREDIT));
        if (metadata.containsKey(META_DATE_CREATED)) {
            doc.setPropertyValue("picture:" + FIELD_DATELINE, metadata.get(
                    META_DATE_CREATED).toString());
        }
        doc.setPropertyValue("picture:" + FIELD_HEADLINE,
                (String) metadata.get(META_HEADLINE));
        doc.setPropertyValue("picture:" + FIELD_LANGUAGE,
                (String) metadata.get(META_LANGUAGE));
        doc.setPropertyValue("picture:" + FIELD_ORIGIN,
                (String) metadata.get(META_OBJECT_NAME));
        doc.setPropertyValue("picture:" + FIELD_SOURCE,
                (String) metadata.get(META_SOURCE));

    }
}
