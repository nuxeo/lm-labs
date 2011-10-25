/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

/**
 * @author fvandaele
 *
 */
public class DeletedPageFilter implements Filter {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(DeletedPageFilter.class);
    

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.core.api.Filter#accept(org.nuxeo.ecm.core.api.DocumentModel)
     */
    @Override
    public boolean accept(DocumentModel docModel) {
        if(docModel != null) {
            try {
                return LabsSiteConstants.State.DELETE.getState().equals(docModel.getCurrentLifeCycleState());
            } catch (ClientException e) {
                log.error(e.getMessage());
            }
        }
        return false;
    }
}
