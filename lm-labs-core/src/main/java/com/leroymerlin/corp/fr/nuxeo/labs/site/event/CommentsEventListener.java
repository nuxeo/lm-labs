package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.comment.api.CommentEvents;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class CommentsEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(CommentsEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        if (!(CommentEvents.COMMENT_ADDED.equals(eventName) || CommentEvents.COMMENT_REMOVED.equals(eventName))) {
            return;
        }
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        CoreSession coreSession = ctx.getCoreSession();
        if (doc != null) {
            if (!Docs.PAGELIST_LINE.type().equals(doc.getType())) {
                return;
            }
            PageListLine lineAdapter = Tools.getAdapter(PageListLine.class, doc, coreSession);
            if (lineAdapter == null) {
                return;
            }
            LOG.debug(eventName + " " + doc.getPathAsString());
            if (CommentEvents.COMMENT_ADDED.equals(eventName)){
                lineAdapter.addComment();
            }
            else if(CommentEvents.COMMENT_REMOVED.equals(eventName)){
                lineAdapter.removeComment();
            }
            coreSession.saveDocument(doc);
            coreSession.save();
        }
    }
}
