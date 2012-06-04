/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import com.leroymerlin.common.core.periode.PeriodInvalidException;
import com.leroymerlin.common.core.periode.Periode;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;

/**
 * @author fvandaele
 *
 */
public class PageNewsFilter implements Filter {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(PageNewsFilter.class);
    
    private Calendar dateComparaison;   

    public PageNewsFilter(Calendar pJour) {
        this.dateComparaison = pJour;
    }


    /* (non-Javadoc)
     * @see org.nuxeo.ecm.core.api.Filter#accept(org.nuxeo.ecm.core.api.DocumentModel)
     */
    @Override
    public boolean accept(DocumentModel docModel) {
    	//No need session because the method getStartPublication and getEndPublication doesn't nedd
        LabsNews news = docModel.getAdapter(LabsNews.class);
        if(news != null) {
            try {
                Calendar startPublication = news.getStartPublication();
                Calendar endPublication = news.getEndPublication();
                
                Calendar dateDebutJour = new GregorianCalendar(dateComparaison.get(Calendar.YEAR), dateComparaison.get(Calendar.MONTH),dateComparaison.get(Calendar.DAY_OF_MONTH));
                Calendar dateFinJour = new GregorianCalendar(dateComparaison.get(Calendar.YEAR), dateComparaison.get(Calendar.MONTH),dateComparaison.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                
                if (endPublication == null){
                    endPublication = (Calendar)dateFinJour.clone();
                    endPublication.add(Calendar.MONTH, 1);
                }
                
                return Periode.isPeriodsOverlap(new Periode(dateDebutJour,
                        dateFinJour), new Periode(startPublication, endPublication));
            } catch (PeriodInvalidException e) {
                return false;
            }
            catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }
}
