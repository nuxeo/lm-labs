package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;

public class LabsCategoyHelper {

    private static final Log LOG = LogFactory.getLog(LabsCategoyHelper.class);
    
    public static final DocumentModelList ALL_CATEGORIES = DirectoriesUtils.getDirDocumentModelList(Directories.CATEGORY);

    public LabsCategoyHelper() {}
    
    public static List<DocumentModel> getCategories() {
        List<DocumentModel> result = new ArrayList<DocumentModel>();
        int parent = 0;
        try {
            for (DocumentModel cat: ALL_CATEGORIES){
                parent = ((Long)cat.getProperty(LabsSiteConstants.Schemas.LABS_CATEGORY.getName(), "parent")).intValue();
                if (parent == 0){
                    result.add(cat);
                }
            }
        } catch (Exception e) {
            LOG.error("Can't get parents of categories !", e);
        }
        return result;
    }
    
    public static List<DocumentModel> getChildrenCategories(DocumentModel currentCategory) throws PropertyException, NullException, ClientException{
        List<DocumentModel> result = new ArrayList<DocumentModel>();
        int idCurrentCategory = ((Long)currentCategory.getProperty(LabsSiteConstants.Schemas.LABS_CATEGORY.getName(), "id")).intValue();
        if (idCurrentCategory != 0){
            int parent;
            for (DocumentModel cat: ALL_CATEGORIES){
                parent = ((Long)cat.getProperty(LabsSiteConstants.Schemas.LABS_CATEGORY.getName(), "parent")).intValue();
                if (parent == idCurrentCategory){
                    result.add(cat);
                }
            }
        }
        return result;
    }
    
    public static List<DocumentModel> getAllCategoriesWithoutGroup() throws PropertyException, NullException, ClientException{
        List<DocumentModel> result = new ArrayList<DocumentModel>();
        for (DocumentModel cat: ALL_CATEGORIES){
            if (getChildrenCategories(cat).size() == 0){
                result.add(cat);
            }
        }
        return result;
    }
    
    public static DocumentModel getCategory(int idCategory) throws ClientException{
        int idCurrentCategory = -1;
        for (DocumentModel cat: ALL_CATEGORIES){
            idCurrentCategory = ((Long)cat.getProperty(LabsSiteConstants.Schemas.LABS_CATEGORY.getName(), "id")).intValue();
            if (idCurrentCategory == idCategory){
                return cat;
            }
        }
        return null;
    }

}
