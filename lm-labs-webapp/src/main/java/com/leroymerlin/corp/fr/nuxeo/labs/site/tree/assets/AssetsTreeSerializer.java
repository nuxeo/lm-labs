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

import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractJSONSerializer;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.CkEditorParametersAdapter;

public class AssetsTreeSerializer extends AbstractJSONSerializer {
    @Override
    public String getUrl(TreeItem item) {
        WebContext ctx = WebEngine.getActiveContext();
        String endUrl = "";
        String calledRefParameter = ctx.getRequest().getParameter(CkEditorParametersAdapter.PARAM_NAME_CALLED_REFERENCE);
        String callbackFunctionParameter = ctx.getRequest().getParameter(CkEditorParametersAdapter.PARAM_NAME_CALLBACK);
        if (calledRefParameter != null && callbackFunctionParameter != null) {
            endUrl = "?" + CkEditorParametersAdapter.PARAM_NAME_CALLBACK + "=" + callbackFunctionParameter + "&" + CkEditorParametersAdapter.PARAM_NAME_CALLED_REFERENCE + "=" + calledRefParameter;
        }
        try {
            StringBuilder sb = new StringBuilder(getBasePath(ctx));
            sb.append(URIUtils.quoteURIPathComponent(item.getPath()
                    .toString(), false)).append(endUrl);
            return sb.toString();
        } catch (ClientException e) {
            return "#";
        }
    }

    @Override
    protected String getBasePath(WebContext ctx) throws ClientException {
        StringBuilder sb = new StringBuilder(ctx.getModulePath());
        LabsSite site = (LabsSite) ctx.getProperty("site");
        sb.append("/" + URIUtils.quoteURIPathComponent(site.getURL(), true) + "/@assets");
        return sb.toString();
    }

}
