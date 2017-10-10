package biz.iws.cache;

import javax.annotation.PostConstruct;

public abstract class AbstractCachingBean<T> implements CachingBean<T> {

	/** cache object */
	protected T cache;

	private boolean isInit = false;

	@PostConstruct
	public void postConstruct() {
		init();
		isInit = true;
	}

	public void refresh() {
		this.cache = createCacheData();
	}

	public void init() {
		this.cache = createCacheData();
	}

	public boolean isInit() {
		return isInit;
	}
}
