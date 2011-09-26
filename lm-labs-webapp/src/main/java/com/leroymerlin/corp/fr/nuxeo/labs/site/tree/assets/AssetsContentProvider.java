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

package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.assets;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractContentProvider;

/**
 * Implementation of provider for the tree.
 */
public class AssetsContentProvider extends AbstractContentProvider {

    private static final long serialVersionUID = 1L;


    public static class FolderFilter implements Filter {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public boolean accept(DocumentModel doc) {
            return doc.isFolder();
        }

    }

    private static Filter filter = new FolderFilter();

    public AssetsContentProvider(CoreSession session) {
        super(session);
    }

    @Override
    public Filter getDocFilter() {
        return filter;
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
