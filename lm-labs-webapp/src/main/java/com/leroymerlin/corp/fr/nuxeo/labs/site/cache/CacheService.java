package com.leroymerlin.corp.fr.nuxeo.labs.site.cache;

import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import org.apache.shindig.common.util.ResourceLoader;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class CacheService extends DefaultComponent {

	private CacheManager cacheManager;

	@Override
	public void activate(ComponentContext context) throws Exception {
		super.activate(context);
		String confLocation = Framework.getProperty("freemarker.labs.site.cache.config",
				"res://com/leroymerlin/corp/fr/nuxeo/labs/site/cache/ehcache-config.xml");


		cacheManager = new CacheManager(getConfiguration(confLocation));

	}

	protected Configuration getConfiguration(String configPath)
			throws IOException {
		InputStream configStream = ResourceLoader.open(configPath);
		return ConfigurationFactory.parseConfiguration(configStream);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {

		if (CacheManager.class == adapter) {
			return adapter.cast(getCacheManager());
		}
		return super.getAdapter(adapter);
	}

	private CacheManager getCacheManager() {
		return cacheManager;
	}

}
