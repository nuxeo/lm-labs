package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.CONVERSION_FORMAT;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.JPEG_CONVERSATION_FORMAT;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPERATION_CROP;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPERATION_RESIZE;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPTION_CROP_X;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPTION_CROP_Y;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPTION_RESIZE_DEPTH;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPTION_RESIZE_HEIGHT;
import static org.nuxeo.ecm.platform.picture.api.ImagingConvertConstants.OPTION_RESIZE_WIDTH;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.platform.picture.api.ImageInfo;
import org.nuxeo.ecm.platform.picture.api.ImagingService;
import org.nuxeo.ecm.platform.picture.api.adapters.PictureBlobHolder;
import org.nuxeo.ecm.platform.picture.api.adapters.PictureResourceAdapter;
import org.nuxeo.runtime.api.Framework;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsBlobHolderException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.bean.CropCoord;

public class LabsNewsBlobHolder extends PictureBlobHolder {

    public static final int MAX_HEIGHT = 600;

    public static final int MAX_WIDTH = 800;

    public static final int MAX_SUMMARY_HEIGHT = 90;

    public static final int MAX_SUMMARY_WIDTH = 120;

    private static final String IMPOSSIBLE_TO_CREATE_TRUNCATED_PICTURE = "Impossible to create truncated picture";

    public static final String SUMMARY_TRUNCATED_PICTURE = "summary_truncated_picture";

    private static final Log log = LogFactory.getLog(LabsNewsBlobHolder.class);
    
    private static final String PREFIX_ACCORDEON = "Accordeon";
    
    private int currentIndexAccordeon;
    
    private PictureResourceAdapter picture;

    private ConversionService converionService;

    private ImagingService imagingService;

	public LabsNewsBlobHolder(DocumentModel doc, String xPath) {
		super(doc, xPath);
		picture = doc.getAdapter(PictureResourceAdapter.class);
		try {
            currentIndexAccordeon = doc.getProperty("picture:views").getChildren().size();
        } catch (Exception e) {
            currentIndexAccordeon = 0;
        }
	}
	
	private Blob formatOriginalBlob(Blob originalBlob) throws ClientException{
	    ImageInfo imageInfo = getImagingService().getImageInfo(originalBlob);
        int originalWidth = 0;
        int originalHeight = 0;
        int originalDepth = 0;
        if (imageInfo != null) {
            originalWidth = imageInfo.getWidth();
            originalHeight = imageInfo.getHeight();
            originalDepth = imageInfo.getDepth();
        }
        else{
            throw new ClientException("Impossible to extract info on " + originalBlob.getFilename());
        }
        
        Point size = new Point(originalWidth, originalHeight);
        size = getSize(size);
        Blob blob = originalBlob;
        if (size.x != originalWidth || size.y != originalHeight){
            blob = resize(originalBlob, size, originalDepth);
        }
	    return blob;
	}

    /**
     * @param originalBlob
     * @param originalWidth
     * @param originalHeight
     * @param originalDepth
     * @return
     * @throws ConversionException
     * @throws ClientException
     */
    private Blob resize(Blob originalBlob, Point size, int originalDepth) throws ConversionException,
            ClientException {
        Blob blob = originalBlob;
        Map<String, Serializable> options = new HashMap<String, Serializable>();
        options.put(OPTION_RESIZE_WIDTH, size.x);
        options.put(OPTION_RESIZE_HEIGHT, size.y);
        options.put(OPTION_RESIZE_DEPTH, originalDepth);
        options.put(CONVERSION_FORMAT, imagingService.getConfigurationValue(CONVERSION_FORMAT,
            JPEG_CONVERSATION_FORMAT));
        BlobHolder bh = new SimpleBlobHolder(originalBlob);
        bh = getConversionService().convert(OPERATION_RESIZE, bh, options);

        //Blob blob = bh.getBlob() != null ? bh.getBlob() : originalBlob;
        if (bh.getBlob() != null){
            blob = bh.getBlob();
            blob.setFilename(originalBlob.getFilename());
            blob.setMimeType(originalBlob.getMimeType());
        }

        return blob;
    }

    //@SuppressWarnings("unchecked")
    @Override
    public void setBlob(Blob originalBlob) throws ClientException {
        Blob blob = formatOriginalBlob(originalBlob);
        xPathFilename = null;
        // check if there are templates
        ArrayList<Map<String, Object>> pictureTemplates = new ArrayList<Map<String,Object>>();
        //add original jpeg for operation
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("description", "Original Picture");
        map.put("tag", "original");
        map.put("title", "Original");
        pictureTemplates.add(map);

        //add original jpeg for operation
        map = new HashMap<String, Object>();
        map.put("description", "Original Picture in JPEG format");
        map.put("tag", "originalJpeg");
        map.put("title", "OriginalJpeg");
        pictureTemplates.add(map);
        
        // upload blob and create views
        String filename = blob == null ? null : blob.getFilename();
        String title = (String) doc.getProperty("dublincore", "title"); // re-set
        try {
            picture.createPicture(blob, filename, title, pictureTemplates);
            //{"x":571,"y":510,"x2":691,"y2":600,"w":120,"h":90}
            doc.getProperty("picture:cropCoords").setValue(this.calculateCropCoords(blob));
            createTruncatedPicture();
        } catch (IOException e) {
            throw new ClientException(e.toString(), e);
        }
    }
    
    /**
     * @param blob
     * @return ex: {"x":571,"y":510,"x2":691,"y2":600,"w":120,"h":90}
     * @throws ClientException
     */
    public String calculateCropCoords(Blob blob) throws ClientException{
        ImageInfo imageInfo = getImagingService().getImageInfo(blob);
        if (imageInfo == null){
            throw new ClientException("Impossible to extract info on " + blob.getFilename());
        }
        StringBuilder result = new StringBuilder("{\"x\":");
        result.append((imageInfo.getWidth() / 2) -60).append(",");
        result.append("\"y\":");
        result.append((imageInfo.getHeight() / 2) - 45).append(",");
        result.append("\"x2\":");
        result.append((imageInfo.getWidth() / 2) + 60).append(",");
        result.append("\"y2\":");
        result.append((imageInfo.getHeight() / 2) + 45).append(",");
        result.append("\"w\":120,\"h\":90}");
        return result.toString();
    }
    
    private Map<String, Serializable> createView(Blob blob, String title) throws IOException {
        Map<String, Serializable> view = new HashMap<String, Serializable>();
        view.put("title", title);
        view.put("content", new FileBlob(blob.getStream()));
        view.put("filename", blob.getFilename());
        return view;
    }
    
    public void addAccordeonPicture(Blob blob) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        views.add(createView(blob, PREFIX_ACCORDEON + currentIndexAccordeon));
        currentIndexAccordeon++;
        doc.getProperty("picture:views").setValue(views);
    }
    
    public void addAccordeonPictures(List<Blob> blobs) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        for (Blob blob:blobs){
            views.add(createView(blob, PREFIX_ACCORDEON + currentIndexAccordeon));
            currentIndexAccordeon++;
        }
        doc.getProperty("picture:views").setValue(views);
    }
    
    public void setAccordeonPictures(List<Blob> blobs) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        List<Map<String, Serializable>> resultViews = new ArrayList<Map<String,Serializable>>();
        for (Map<String, Serializable> view:views){
            if (!((String)view.get("title")).startsWith(PREFIX_ACCORDEON)){
                resultViews.add(view);
            }
        }
        doc.getProperty("picture:views").setValue(resultViews);
        addAccordeonPictures(blobs);
    }
    
    public void clearAccordeon() throws ClientException{
        this.currentIndexAccordeon = 0;
//        List<Map<String, Serializable>> newViews = new ArrayList<Map<String, Serializable>>();
//        Collection<Property> views = doc.getProperty("picture:views").getChildren();
//        for (Property property : views) {
//            if (((String)property.getValue("title")).startsWith(PREFIX_ACCORDEON)){
//                newViews.ad
//            }
//            blobList.add((Blob) property.getValue("content"));
//        }
    }
    
    public void setCropCoords(String cropCoords) throws ClientException{
        String oldValue = (String)doc.getProperty("picture:cropCoords").getValue();
        if (StringUtils.isEmpty(oldValue) || !oldValue.equals(cropCoords)){
            doc.getProperty("picture:cropCoords").setValue(cropCoords);
            createTruncatedPicture();
        }
    }
    
    private void createTruncatedPicture() throws PropertyException, ClientException {
        String cropCoords = (String)doc.getProperty("picture:cropCoords").getValue();
        if (StringUtils.isEmpty(cropCoords)){
            throw new ClientException(IMPOSSIBLE_TO_CREATE_TRUNCATED_PICTURE + ";No cropCoords !");
        }
        Blob originalJpeg = picture.getPictureFromTitle("OriginalJpeg");
        if (originalJpeg != null && originalJpeg.getLength() > 0){
            Gson gson = new Gson();
            Type mapType = new TypeToken<CropCoord>() {}.getType();
            CropCoord crop = gson.fromJson(cropCoords, mapType);
            Map<String, Serializable> coords = new HashMap<String, Serializable>();
            coords.put("x", crop.getX());
            coords.put("y", crop.getY());
            coords.put("h", crop.getH());
            coords.put("w", crop.getW());
            Blob truncatedBlob = this.crop(originalJpeg, coords);
            
            ImageInfo imageInfo = getImagingService().getImageInfo(truncatedBlob);
            if (imageInfo != null) {
                //le blob a deja un ratio 4/3 a ce moment,
                //donc on ne test qu'une seule dimension
                //Le crop a une taille minimum
                if(imageInfo.getWidth() > MAX_SUMMARY_WIDTH){
                    truncatedBlob = resize(truncatedBlob, new Point(MAX_SUMMARY_WIDTH, MAX_SUMMARY_HEIGHT), imageInfo.getDepth());
                }
            }
            try {
                addView(truncatedBlob, SUMMARY_TRUNCATED_PICTURE);
            } catch (IOException e) {
                throw new ClientException(IMPOSSIBLE_TO_CREATE_TRUNCATED_PICTURE, e);
            }
        }
        else {
            throw new ClientException(IMPOSSIBLE_TO_CREATE_TRUNCATED_PICTURE + ";No view 'OriginalJpeg' !");
        }
    }
    
    public void addView(Blob blob, String title) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        boolean isCreate = true;
        for(Map<String, Serializable> view:views){
            if (title != null && title.equals((String)view.get("title"))){
                view.put("content", new FileBlob(blob.getStream()));
                view.put("filename", blob.getFilename());
                isCreate = false;
                break;
            }
        }
        if (isCreate){
            views.add(createView(blob, title));
        }
        doc.getProperty("picture:views").setValue(views);
    }

    public String getCropCoords() throws ClientException{
        return (String)doc.getProperty("picture:cropCoords").getValue();
    }

    public List<Blob> getAccordeonBlobs() throws ClientException{
        List<Blob> blobList = new ArrayList<Blob>();
        Collection<Property> views = doc.getProperty("picture:views").getChildren();
        for (Property property : views) {
            if (((String)property.getValue("title")).startsWith(PREFIX_ACCORDEON)){
                blobList.add((Blob) property.getValue("content"));
            }
        }
        return blobList;
    }

    public Blob crop(Blob blob, Map<String, Serializable> coords)
            throws ClientException {
        try {
            BlobHolder bh = new SimpleBlobHolder(blob);
            String type = blob.getMimeType();

            Map<String, Serializable> options = new HashMap<String, Serializable>();
            options.put(OPTION_CROP_X, coords.get("x"));
            options.put(OPTION_CROP_Y, coords.get("y"));
            options.put(OPTION_RESIZE_HEIGHT, coords.get("h"));
            options.put(OPTION_RESIZE_WIDTH, coords.get("w"));

            if (type != "image/png") {
                bh = getConversionService().convert(OPERATION_CROP, bh, options);
                return bh.getBlob();
            }
        } catch (Exception e) {
            throw new ClientException("Crop failed", e);
        }
        return null;
    }

    protected ConversionService getConversionService() throws ClientException {
        if (converionService == null) {
            try {
                converionService = Framework.getService(ConversionService.class);
            } catch (Exception e) {
                log.error("Unable to get conversion Service.", e);
                throw new ClientException(e);
            }
        }
        return converionService;
    }

    protected ImagingService getImagingService() {
        if (imagingService == null) {
            try {
                imagingService = Framework.getService(ImagingService.class);
            } catch (Exception e) {
                log.error("Unable to get Imaging Service.", e);
            }

        }
        return imagingService;
    }

    private static Point getSize(Point current) {
        return getSizeMin(getSizeMax(current, MAX_WIDTH, MAX_HEIGHT), MAX_SUMMARY_WIDTH, MAX_SUMMARY_HEIGHT);
    }

    private static Point getSizeMax(Point current, int maxWidth, int maxHeight) {
        int x = current.x;
        int y = current.y;
        int newx;
        int newy;
        if (x > y) { // landscape
            newy = (y * maxWidth) / x;
            newx = maxWidth;
        } else { // portrait
            newx = (x * maxHeight) / y;
            newy = maxHeight;
        }
        if (newx > x || newy > y) {
            return current;
        }
        return new Point(newx, newy);
    }

    private static Point getSizeMin(Point current, int minWidth, int minHeight) {
        int x = current.x;
        int y = current.y;
        int newx;
        int newy;
        if (x > y) { // landscape
            newy = (y * minWidth) / x;
            newx = minWidth;
        } else { // portrait
            newx = (x * minHeight) / y;
            newy = minHeight;
        }
        if (newx < x && newy < y) {
            return current;
        }
        return new Point(minWidth, minHeight);
    }
    
    public void checkPicture(Blob blob) throws LabsBlobHolderException, ClientException{
        if (blob == null){
            throw new ClientException("The blob is null !");
        }
        if (!blob.getMimeType().startsWith("image/")){
            //log.in
            throw new LabsBlobHolderException("label.labsNews.news_notupdated.notimage");
        }
        ImageInfo imageInfo = getImagingService().getImageInfo(blob);
        if (imageInfo == null){
            throw new LabsBlobHolderException("label.labsNews.news_notupdated.notinfoimage");
        }
        if(!(imageInfo.getWidth() >= MAX_SUMMARY_WIDTH && imageInfo.getHeight() >= MAX_SUMMARY_HEIGHT)){
            throw new LabsBlobHolderException("label.labsNews.news_notupdated.size");
        }
    }

    public void deleteSummaryPicture() throws ClientException {
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        List<Map<String, Serializable>> newViews = new ArrayList<Map<String,Serializable>>();
        for(Map<String, Serializable> view:views){
            if (!isSummaryPicture((String)view.get("title"))){
                newViews.add(view);
            }
        }
        doc.getProperty("picture:views").setValue(newViews);
    }

    private boolean isSummaryPicture(String string) {
        if (StringUtils.isEmpty(string)){
            return false;
        }
        boolean result = string.equals("Original");
        result = result || string.equals("OriginalJpeg");
        result = result || string.equals("summary_truncated_picture");
        return result;
    }
}
