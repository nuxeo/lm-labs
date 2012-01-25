package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs.PAGECLASSEUR;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {

        if (klass.equals(PageClasseur.class) && PAGECLASSEUR.type()
                .equals(doc.getType())) {
            return new PageClasseurAdapter(doc);
        }

        if (klass.equals(PageClasseurFolder.class)
                && Docs.PAGECLASSEURFOLDER.type().equals(doc.getType())) {
            return new PageClasseurFolderImpl(doc);
        }

        return null;
    }

}
