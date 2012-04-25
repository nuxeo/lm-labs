package com.leroymerlin.corp.fr.nuxeo.labs.site.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhCacheWrapper<K,V> implements CacheWrapper<K,V>{

	private final String cacheName;
    private final CacheManager cacheManager;
	private Cache cache;

    public EhCacheWrapper(final String cacheName, final CacheManager cacheManager)
    {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;
    }

    public void put(final K key, final V value)
    {
        getCache().put(new Element(key, value));
    }

    @SuppressWarnings("unchecked")
    public V get(final K key)
    {
        Element element = getCache().get(key);
        if (element != null) {
            return (V) element.getValue();
        }
        return null;
    }

    public Ehcache getCache()
    {

    	synchronized (cacheManager) {
			cache = cacheManager.getCache(cacheName);
    	      if (cache == null) {
    	        cacheManager.addCache(cacheName);
    	        cache = cacheManager.getCache(cacheName);
    	        if (cache == null) {
    	          throw new RuntimeException("Failed to create Cache with name " + cacheName);
    	        }
    	      }
    	    }
        return cacheManager.getEhcache(cacheName);
    }

}
