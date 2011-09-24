/*
 * (C) Copyright 2006-2009 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.ui.tree.document.DocumentContentProvider;

/**
 * Implementation of provider for the tree.
 */
public class SiteContentProvider extends DocumentContentProvider {

    private static final long serialVersionUID = 1L;

    public SiteContentProvider(CoreSession session) {
        super(session);
    }

    @Override
    public Object[] getChildren(Object obj) {
        Object[] objects = super.getChildren(obj);
        List<Object> v = new Vector<Object>();
        for (Object o : objects) {
            DocumentModel doc = (DocumentModel) o;
            if(doc.isFolder()) {
                v.add(doc);
            }
        }
        return v.toArray();
    }

    @Override
    public String getLabel(Object obj) {
        String label = super.getLabel(obj);
        return label == null ? null : StringEscapeUtils.escapeXml(label);
    }

    @Override
    public boolean isContainer(Object obj) {

        if (!super.isContainer(obj)) {
            return false;
        }
        if (obj instanceof DocumentModel) {
            DocumentModel doc = (DocumentModel) obj;
            CoreSession session = doc.getCoreSession();
            try {
                return session.getChildren(doc.getRef(), "Folder")
                        .size() > 0;
            } catch (ClientException e) {
                return false;
            }
        }
        return false;

    }

}
