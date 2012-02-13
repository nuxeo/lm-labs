package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.platform.picture.api.adapters.PictureBlobHolder;
import org.nuxeo.ecm.platform.picture.api.adapters.PictureResourceAdapter;

public class LabsNewsBlobHolder extends PictureBlobHolder {
    
    private static final String PREFIX_ACCORDEON = "Accordeon";
    private int currentIndexAccordeon;
    private PictureResourceAdapter picture;

	public LabsNewsBlobHolder(DocumentModel doc, String xPath) {
		super(doc, xPath);
		picture = doc.getAdapter(PictureResourceAdapter.class);
		try {
            currentIndexAccordeon = doc.getProperty("picture:views").getChildren().size();
        } catch (Exception e) {
            currentIndexAccordeon = 0;
        }
	}

    //@SuppressWarnings("unchecked")
    @Override
    public void setBlob(Blob blob) throws ClientException {
        xPathFilename = null;
        // check if there are templates
        ArrayList<Map<String, Object>> pictureTemplates = new ArrayList<Map<String,Object>>();
        
        //TODO AGAINST
        /*DocumentModel parent = doc.getCoreSession().getParentDocument(
                doc.getRef());
        if (parent.getType().equals("PictureBook")) {
            // use PictureBook Properties
            pictureTemplates = (ArrayList<Map<String, Object>>) parent.getProperty(
                    "picturebook", "picturetemplates");
            if (pictureTemplates.isEmpty()) {
                pictureTemplates = null;
            }
        }*/
        //add original jpeg for operation
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("maxsize", new Long(blob.getLength()));
        map.put("description", "Original Picture");
        map.put("tag", "original");
        map.put("title", "Original");
        pictureTemplates.add(map);

        //add original jpeg for operation
        map = new HashMap<String, Object>();
        map.put("maxsize", new Long(blob.getLength()));
        map.put("description", "Original Picture in JPEG format");
        map.put("tag", "originalJpeg");
        map.put("title", "OriginalJpeg");
        pictureTemplates.add(map);
        
        // upload blob and create views
        String filename = blob == null ? null : blob.getFilename();
        String title = (String) doc.getProperty("dublincore", "title"); // re-set
        try {
            picture.createPicture(blob, filename, title, pictureTemplates);
        } catch (IOException e) {
            throw new ClientException(e.toString(), e);
        }
    }
    
    private Map<String, Serializable> createPicture(Blob blob, String title) throws IOException {
        Map<String, Serializable> view = new HashMap<String, Serializable>();
        view.put("title", title);
        view.put("content", new FileBlob(blob.getStream()));
        view.put("filename", blob.getFilename());
        return view;
    }
    
    public void addAccordeonPicture(Blob blob) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        views.add(createPicture(blob, PREFIX_ACCORDEON + currentIndexAccordeon));
        currentIndexAccordeon++;
        doc.getProperty("picture:views").setValue(views);
    }
    
    public void addAccordeonPictures(List<Blob> blobs) throws ClientException, IOException{
        @SuppressWarnings("unchecked")
        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>)doc.getPropertyValue("picture:views");
        for (Blob blob:blobs){
            views.add(createPicture(blob, PREFIX_ACCORDEON + currentIndexAccordeon));
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
            this.createTruncatedPicture();
        }
    }
    
    private void createTruncatedPicture() {
        // TODO Auto-generated method stub
        
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
}
