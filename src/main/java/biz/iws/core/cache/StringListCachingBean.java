package biz.iws.core.cache;

import java.util.List;

public abstract class StringListCachingBean extends AbstractCachingBean<List<String>> {

	public String get(int index) {
		return cache.get(index);
	}

	public List<String> get() {
		return cache;
	}

}
