package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.video;

import static org.nuxeo.ecm.platform.video.VideoConstants.STORYBOARD_PROPERTY;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentLocationImpl;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;
import org.nuxeo.ecm.platform.video.TranscodedVideo;
import org.nuxeo.ecm.platform.video.VideoConstants;
import org.nuxeo.ecm.platform.video.VideoConversionStatus;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.service.VideoConversion;
import org.nuxeo.ecm.platform.video.service.VideoConversionId;
import org.nuxeo.ecm.platform.video.service.VideoService;
import org.nuxeo.ecm.platform.web.common.UserAgentMatcher;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

@WebAdapter(name = "labsvideo", type = "LabsVideo", targetType = "Document", targetFacets = {VideoConstants.VIDEO_FACET})
public class LabsVideoAdapter extends DefaultAdapter {

    private static final Log LOG = LogFactory.getLog(LabsVideoAdapter.class);

    @GET
    public Object doGet() {
        return getView("index");
    }
    
    @POST @Path("convert")
    public Object doLaunchConversion(@FormParam("conversion") String conversionName) {
        DocumentObject dobj = (DocumentObject) getTarget();
        launchConversion(dobj.getDocument(), conversionName);
        return Response.ok().build();
    }

    public List<StoryboardItem> getStoryboardItems(DocumentModel doc)
            throws PropertyException, ClientException {
        if (!doc.hasFacet(VideoConstants.HAS_STORYBOARD_FACET)) {
            return Collections.emptyList();
        }
        int size = doc.getProperty(STORYBOARD_PROPERTY).getValue(List.class).size();
        List<StoryboardItem> items = new ArrayList<StoryboardItem>(size);
        for (int i = 0; i < size; i++) {
            items.add(new StoryboardItem(doc, STORYBOARD_PROPERTY, i));
        }
        return items;
    }

    public String getURLForStaticPreview(DocumentModel videoDoc)
            throws ClientException {
        String lastModification = ""
                + (((Calendar) videoDoc.getPropertyValue("dc:modified")).getTimeInMillis());
        return DocumentModelFunctions.fileUrl("downloadPicture", videoDoc,
                "StaticPlayerView:content", lastModification);
    }

    public VideoConversionStatus getVideoConversionStatus(DocumentModel doc, String conversionName) {
        VideoConversionStatus progressStatus = null;
        VideoConversionId id = new VideoConversionId(new DocumentLocationImpl(doc), conversionName);
        try {
            progressStatus = Framework.getService(VideoService.class).getProgressStatus(id);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return progressStatus;
    }

    public String getVideoConversionStatusMessage(VideoConversionStatus progressStatus) {
        String result = "";
        if (progressStatus != null) {
            return progressStatus.getMessage();
        }
        return result;
    }

    public String getTranscodedVideoURL(DocumentModel doc, String name) {
        TranscodedVideo transcodedVideo = getTranscodedVideo(doc, name);
        if (transcodedVideo == null) {
            return null;
        }

        String blobPropertyName = transcodedVideo.getBlobPropertyName();
        return bigFileUrl(doc, blobPropertyName,
                transcodedVideo.getBlob().getFilename());
    }

    public TranscodedVideo getTranscodedVideo(DocumentModel doc, String name) {
        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class);
        return videoDocument.getTranscodedVideo(name);
    }

    public boolean isSafariHTML5() {
        return UserAgentMatcher.isSafari5(getUserAgent());
    }

    public boolean isChromeHTML5() {
        return UserAgentMatcher.isChrome(getUserAgent());
    }

    public boolean isFirefoxHTML5() {
        return UserAgentMatcher.isFirefox4OrMore(getUserAgent());
    }

    public Collection<VideoConversion> getAvailableVideoConversions() {
        try {
            return Framework.getService(VideoService.class).getAvailableVideoConversions();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    protected void launchConversion(DocumentModel doc, String conversionName) {
        try {
            Framework.getService(VideoService.class).launchConversion(doc, conversionName);
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }
    
    protected String getUserAgent() {
        HttpServletRequest request = getContext().getRequest();
        return request.getHeader("User-Agent");
    }

    private String bigFileUrl(DocumentModel doc, String blobPropertyName, String filename) {
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

}
