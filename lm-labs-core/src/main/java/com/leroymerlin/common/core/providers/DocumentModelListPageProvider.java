package com.leroymerlin.common.core.providers;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.query.api.AbstractPageProvider;

public class DocumentModelListPageProvider extends AbstractPageProvider<DocumentModel> {

    private static final long serialVersionUID = 1L;

    private DocumentModelList documents;

    protected List<DocumentModel> currentPageDocuments;

    public DocumentModelListPageProvider(DocumentModelList list) {
        this.documents = list;
    }

    @Override
    public List<DocumentModel> getCurrentPage() {
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;
            
            currentPageDocuments = new ArrayList<DocumentModel>();
            resultsCount = documents.size();
            long pageSize = getMinMaxPageSize();
            currentPageDocuments = new ArrayList<DocumentModel>();
            if (pageSize == 0) {
                currentPageDocuments.addAll(documents);
            } else {
                // handle offset
                if (offset <= resultsCount) {
                    for (int i = Long.valueOf(offset).intValue(); i < resultsCount
                            && i < offset + pageSize; i++) {
                        currentPageDocuments.add(documents.get(i));
                    }
                }
            }
        }
        return currentPageDocuments;
    }
    
    @Override
    protected void pageChanged() {
        super.pageChanged();
        currentPageDocuments = null;
    }

    @Override
    public void refresh() {
        super.refresh();
        currentPageDocuments = null;
    }

}
