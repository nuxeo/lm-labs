package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

public class NewsPublicationEventListener extends PageNewsNotifier implements EventListener {

    private static final Log LOG = LogFactory.getLog(NewsPublicationEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        if (doc != null) {
            LabsNews labsNews = doc.getAdapter(LabsNews.class);
            if (labsNews == null) {
                return;
            }
            LOG.debug("event: " + eventName);
            if (!DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName) && !DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)) {
                return;
            }
            DocumentModel pageNewsDoc = ctx.getCoreSession().getDocument(doc.getParentRef());
            PageNews pageNews = pageNewsDoc.getAdapter(PageNews.class);
            List<LabsNews> publishedNews = pageNews.getTopNews(Integer.MAX_VALUE);
            if (!publishedNews.contains(labsNews)) {
                return;
            }
            try {
                if (State.PUBLISH.getState().equals(pageNewsDoc.getCurrentLifeCycleState())) {
                    fireEvent(pageNewsDoc);
                }
            } catch (Exception e) {
                throw new ClientException(e.getCause());
            }
        }

    }
    
}
