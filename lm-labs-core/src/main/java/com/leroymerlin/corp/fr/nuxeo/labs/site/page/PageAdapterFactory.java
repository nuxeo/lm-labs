package com.leroymerlin.corp.fr.nuxeo.labs.site.page;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.PageBlocsAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPageImpl;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNewsAdapter;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if(Docs.HTMLPAGE.type().equals(doc.getType())) {
            return new HtmlPageImpl(doc);
        } else if (Docs.SITE.type().equals(doc.getType())) {
            return new LabsSiteAdapter(doc);
        } else if (Docs.PAGEBLOCS.type().equals(doc.getType())) {
            return new PageBlocsAdapter(doc);
        }else if (Docs.PAGECLASSEUR.type().equals(doc.getType())) {
            return new PageClasseurAdapter(doc);
        }else if (Docs.PAGELIST.type().equals(doc.getType())) {
            return new PageListAdapter(doc);
        }else if (Docs.PAGENEWS.type().equals(doc.getType())) {
            return new PageNewsAdapter(doc);
        } else if(Docs.DASHBOARD.type().equals(doc.getType())) {
            return new PageAdapter(doc);
        } else if(doc.hasSchema("page")) {
            return new PageAdapter(doc);
        }
        return null;
    }

}
