/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.sort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Sorter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;

/**
 * @author fvandaele
 *
 */
public class PageNewsSorter implements Sorter {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(PageNewsSorter.class);

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(DocumentModel pDoc1, DocumentModel pDoc2) {
        LabsNews news1 = pDoc1.getAdapter(LabsNews.class);
        LabsNews news2 = pDoc2.getAdapter(LabsNews.class);
        try {
            return news2.getStartPublication().compareTo(news1.getStartPublication());
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

}
