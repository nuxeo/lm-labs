package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

public class AuthorFullName {
    private static final String IMPOSSIBLE_TO_GET_SERVICE_USER_MANAGER = "Impossible to get service UserManager !";
    
    private Map<String, String> mapUserName = null;
    private String propertyValueAuthor = null;
    
    public AuthorFullName(Map<String, String> mapUserName, String propertyValueAuthor) {
        this.mapUserName = mapUserName;
        this.propertyValueAuthor = propertyValueAuthor;
    }
    
    public void loadFullName(List<DocumentModel> documents) throws ClientException {
        mapUserName = new HashMap<String, String>();
        if (!documents.isEmpty()){
            UserManager userManager = null;
            try {
                userManager = Framework.getService(UserManager.class);
            } catch (Exception e) {
                throw new ClientException(IMPOSSIBLE_TO_GET_SERVICE_USER_MANAGER, e);
            }
            String author = null;
            NuxeoPrincipal user = null;
            for(DocumentModel document : documents){
                author = (String)document.getPropertyValue(propertyValueAuthor);
                if (!mapUserName.containsKey(author)){
                    user = userManager.getPrincipal(author);
                    if (user != null){
                        mapUserName.put(author, user.getFirstName() + " " + user.getLastName());
                    }
                    else{
                        mapUserName.put(author, author);
                    }
                }
            }
        }
    }
    
    public String getFullName(String pAuthor){
        if (mapUserName != null && mapUserName.containsKey(pAuthor)){
            return mapUserName.get(pAuthor);
        }
        return "";
    }


}
