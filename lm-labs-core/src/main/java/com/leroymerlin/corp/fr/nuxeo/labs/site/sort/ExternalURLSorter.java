/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.sort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Sorter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;

/**
 * @author fvandaele
 *
 */
public class ExternalURLSorter implements Sorter {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(ExternalURLSorter.class);

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(DocumentModel pDoc1, DocumentModel pDoc2) {
        ExternalURL ext1 = pDoc1.getAdapter(ExternalURL.class);
        ExternalURL ext2 = pDoc2.getAdapter(ExternalURL.class);
        try {
            return new Integer(ext2.getOrder()).compareTo(new Integer(ext1.getOrder()));
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

}
