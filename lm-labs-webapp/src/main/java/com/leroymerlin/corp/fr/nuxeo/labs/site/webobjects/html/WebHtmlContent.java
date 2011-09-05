package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

@WebObject(type="HtmlContent")
public class WebHtmlContent extends DocumentObject {

	private HtmlContent content;

	@Override
	public void initialize(Object... args) {
		super.initialize(args);
		assert args != null && args.length == 2;
		content = (HtmlContent) args[1];
		RenderingEngine engine = ctx.getEngine().getRendering();
		engine.setSharedVariable("content", content);
	}
	
	@POST
	@Override
	public Response doPost() {
		FormData form = ctx.getForm();
		try {
			content.setHtml(form.getString("content"));
			saveDocument();
			return redirect(prev.getPrevious().getPrevious().getPath());
		} catch (ClientException e) {
			return Response.serverError().build();
		}
		
	}
	
	
	@Override
	public Response doDelete() {
		//Do nothing, but don't delete the doc !
		return redirect(prev.getPrevious().getPrevious().getPath());
	}

	private void saveDocument() throws ClientException {
		CoreSession session = ctx.getCoreSession();
		session.saveDocument(doc);
		session.save();
	}

}
