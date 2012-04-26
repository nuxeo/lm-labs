package com.leroymerlin.corp.fr.nuxeo.labs.pathsegment;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentService;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentServiceDefault;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public class PathSegmentServicePage extends PathSegmentServiceDefault implements PathSegmentService {
    
    @Override
    public String generatePathSegment(DocumentModel doc) throws ClientException {
        this.maxSize = LabsSiteUtils.maxSizeCharacterName;
        
        return LabsSiteUtils.doLabsSlugify(doc.getTitle());
    }
    
    

}
