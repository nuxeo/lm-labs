package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import java.util.List;
import java.util.Vector;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.spaces.impl.docwrapper.DocGadgetImpl;
import org.nuxeo.ecm.webengine.ui.tree.document.DocumentContentProvider;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class SharedElementContentProvider extends DocumentContentProvider {
    
    private static final long serialVersionUID = 1L;


    public SharedElementContentProvider(CoreSession session) {
        super(session);
    }

    @Override
    public Object[] getChildren(Object obj) {
        Object[] objects = super.getChildren(obj);
        List<Object> v = new Vector<Object>();
        for (Object o : objects) {
            DocumentModel doc = (DocumentModel) o;
            if(accept(doc, (DocumentModel)obj)) {
                v.add(doc);
            }
        }
        return v.toArray();
    }

    @Override
    public boolean isContainer(Object obj) {
        if (obj instanceof DocumentModel) {
            DocumentModel doc = (DocumentModel) obj;
            try {
                DocumentModelList children = session.getChildren(doc.getRef(), null, null,null, null);
                for (DocumentModel child:children){
                    if (accept(child, doc)){
                        return true;
                    }
                }
            } catch (ClientException e) {
                return false;
            }
        }
        return false;

    }
    
    private boolean accept (DocumentModel doc, DocumentModel docParent){
        if(LabsSiteConstants.Docs.SITETHEMESROOT.docName().equals(doc.getName())){
            return false;
        }
        if (DocGadgetImpl.TYPE.equals(doc.getType())) {
        	return false;
        }
        if(LabsSiteConstants.Docs.ASSETS.docName().equals(doc.getName())){
            return true;
        }
        if(LabsSiteConstants.Docs.TREE.docName().equals(doc.getName())){
            return true;
        }
        if(Tools.getAdapter(BlobHolder.class, doc, session) != null){
            return true;
        }
        boolean accept = Tools.getAdapter(Page.class, doc, session) != null;
        accept = accept || LabsSiteConstants.Docs.PAGECLASSEURFOLDER.type().equals(doc.getType());
        if(LabsSiteConstants.Docs.PAGECLASSEURFOLDER.type().equals(docParent.getType())){
            accept = true;
        }
        try {
            accept = accept && !LabsSiteConstants.State.DELETE.getState().equals(doc.getCurrentLifeCycleState());
        } catch (ClientException e) {
            accept = false;
        }
        return accept;
    }

}
