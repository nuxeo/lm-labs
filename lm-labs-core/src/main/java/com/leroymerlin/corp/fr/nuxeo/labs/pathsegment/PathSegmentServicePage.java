package com.leroymerlin.corp.fr.nuxeo.labs.pathsegment;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentService;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentServiceDefault;

public class PathSegmentServicePage extends PathSegmentServiceDefault implements PathSegmentService {
    
    @Override
    public String generatePathSegment(DocumentModel doc) throws ClientException {
        this.maxSize = 64;
        return super.generatePathSegment(doc);
    }

}
