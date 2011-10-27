package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.portal.usermanager.LMNuxeoPrincipal;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {

    private static final String ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT = " isn't authorized to display this element!";

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";

    private static final Log LOG = LogFactory.getLog(PageResource.class);

    private static final String[] MESSAGES_TYPE = new String[] { "error",
            "info", "success", "warning" };
    
    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        if (args != null && args.length > 0){
            if (args[0] instanceof DocumentModel){
                try {
                    DocumentModel document = (DocumentModel)args[0];
                    Page page = document.getAdapter(Page.class);
                    boolean authorized = page.isAuthorized(getContext().getPrincipal().getName(), ((LMNuxeoPrincipal) ctx.getPrincipal()).isAnonymous());
                    authorized = authorized && !page.isDeleted();
                    if (!authorized){
                        throw new WebResourceNotFoundException(getContext().getPrincipal().getName() + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT);
                    }
                } catch (ClientException e) {
                    throw new WebResourceNotFoundException(getContext().getPrincipal().getName() + ISN_T_AUTHORIZED_TO_DISPLAY_THIS_ELEMENT, e);
                }
            }
        }
    }

    public Page getPage() {
        return doc.getAdapter(Page.class);
    }

    public Page getPage(DocumentModel pDoc) {
        return pDoc.getAdapter(Page.class);
    }

    @Override
    public <A> A getAdapter(Class<A> adapter) {
        return doc.getAdapter(adapter) != null ? doc.getAdapter(adapter)
                : super.getAdapter(adapter);
    }

    public String escapeJS(String pString) {
        if (StringUtils.isEmpty(pString)) {
            return "";
        }
        return StringEscapeUtils.escapeJavaScript(pString);
    }

    @GET
    @Path("displayBrowseTree")
    public Object doBrowseTree() {
        Template template = getTemplate(BROWSE_TREE_VIEW);
        return template;
    }


    /**
     * Returns a Map containing all "flash" messages
     *
     * @return
     */
    public Map<String, String> getMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        FormData form = ctx.getForm();
        for (String type : MESSAGES_TYPE) {
            String message = form.getString("message_" + type);
            if (StringUtils.isNotBlank(message)) {
                messages.put(type, message);
            }
        }
        return messages;

    }

    @Override
    public Resource initialize(WebContext ctx, ResourceType type, Object... args) {
        // TODO Auto-generated method stub
        return super.initialize(ctx, type, args);
    }
}
