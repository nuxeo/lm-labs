package com.leroymerlin.corp.fr.nuxeo.labs.site.cache;

public interface CacheWrapper<K,V> {
	  void put(K key, V value);

	  V get(K key);
}
