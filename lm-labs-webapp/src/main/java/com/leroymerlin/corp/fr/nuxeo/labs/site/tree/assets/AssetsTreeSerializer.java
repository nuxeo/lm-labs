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

import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractJSONSerializer;

public class AssetsTreeSerializer extends AbstractJSONSerializer {
    
    boolean isCommon;
    
    public AssetsTreeSerializer(){
        this(false);
    }
    
    public AssetsTreeSerializer(boolean isCommon){
        this.isCommon = isCommon;
    }
    
    @Override
    public String getUrl(TreeItem item) {
        WebContext ctx = WebEngine.getActiveContext();
        StringBuilder result = new StringBuilder("javascript:loadContentAsset('");
        StringBuilder sb = new StringBuilder("#");
        try {
            sb = new StringBuilder(getBasePath(ctx));
            String path = item.getPath().toString();
            if (!StringUtils.isEmpty(path) && path.length() > 1){
                sb.append(URIUtils.quoteURIPathComponent(path, false));
            }
            sb.append("/@views/content");
            if (this.isCommon){
                sb.append("?isCommon=true");
            }
            result.append(sb.toString()).append("', ");
            if (this.isCommon){
                result.append("true");
            }
            else{
                result.append("false");
            }
            result.append(");");
        } catch (ClientException e) {
            sb = new StringBuilder("#");
        }
        return result.toString();
    }
    
    @Override
    protected String getBasePath(WebContext ctx) throws ClientException {
        StringBuilder sb = new StringBuilder(ctx.getModulePath());
        LabsSite site = (LabsSite) ctx.getProperty("site");
        sb.append("/" + URIUtils.quoteURIPathComponent(site.getURL(), true));
        sb.append("/@assets");
        return sb.toString();
    }

}
