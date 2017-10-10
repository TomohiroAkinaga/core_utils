package biz.iws.cache;

import java.util.Map;

public abstract class StringKeyValueMapAutoRefreshCachingBean
		extends AbstractAutoRefreshCachingBean<Map<String, String>> {

	public String get(String key) {
		return cache.get(key);
	}

	public String getString(String key, String defaultValue) {
		return cache.get(key) != null ? cache.get(key) : defaultValue;
	}
}
