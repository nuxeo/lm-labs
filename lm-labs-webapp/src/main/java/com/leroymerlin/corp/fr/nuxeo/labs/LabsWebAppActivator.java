/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Sun Seng David TAN <stan@nuxeo.com>
 */
package com.leroymerlin.corp.fr.nuxeo.labs;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Bundle activator, will contain manage to access to the bundle resources
 * shared by the bundle and its fragments
 */
public class LabsWebAppActivator implements BundleActivator {

    public static LabsWebAppActivator getDefault() {
        return defaultActivator;
    }

    protected static LabsWebAppActivator defaultActivator;

    protected BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        defaultActivator = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        defaultActivator = null;
    }

    public BundleContext getContext() {
        return context;
    }

    public Bundle getBundle(){
        return context.getBundle();
    }

}
