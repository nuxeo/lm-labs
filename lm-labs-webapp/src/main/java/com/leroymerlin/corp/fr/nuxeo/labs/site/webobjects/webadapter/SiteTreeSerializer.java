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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.JSonTreeSerializer;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

public class SiteTreeSerializer extends JSonTreeSerializer {

    @Override
    public String getUrl(TreeItem item) {
        WebContext ctx = WebEngine.getActiveContext();

        try {
            StringBuilder sb = new StringBuilder(ctx.getModulePath());
            LabsSite site = (LabsSite) ctx.getProperty("site");
            sb.append("/" + site.getURL() + "/@assets");
            sb.append(URIUtils.quoteURIPathComponent(item.getPath()
                    .toString(), false));
            return sb.toString();
        } catch (ClientException e) {
            return "#";
        }
    }

    /**
     * You may override this method to change the output JSON.
     */
    protected JSONObject item2JSON(TreeItem item, JSONArray children) {
        JSONObject json = new JSONObject();
        json.element("text", item.getLabel())
                .element("id", item.getPath()
                        .toString())
                .element("href", getUrl(item));
        json.element("expanded", item.isExpanded());
        if (item.isContainer()) {
            if (item.hasChildren()) {
                json.element("children", children);
            } else {
                json.element("hasChildren", true);
            }
        } else {
            json.element("hasChildren", false);
        }
        return json;
    }

}
