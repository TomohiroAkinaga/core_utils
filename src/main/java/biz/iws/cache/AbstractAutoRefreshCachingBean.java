package biz.iws.cache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAutoRefreshCachingBean<T> extends AbstractCachingBean<T> {

	private static final Logger log = LoggerFactory.getLogger(AbstractAutoRefreshCachingBean.class);

	protected static final long DEFAULT_REFRESH_RATE = 5 * 60; // 5分

	private ScheduledExecutorService executor;

	protected long refreshRate = DEFAULT_REFRESH_RATE;

	@PostConstruct
	public void postConstruct() {
		setup();
		super.postConstruct();
		startAutoRefresh();
	}

	/**
	 * call fastest by post construct process
	 */
	public abstract void setup();

	protected void restartAutoRefresh(long refreshRate) {
		this.refreshRate = refreshRate;
		stopAutoRefresh();
		startAutoRefresh();
	}

	private final void startAutoRefresh() {
		executor = Executors.newSingleThreadScheduledExecutor();
		log.info("start scheduled thread start process. [{}]", this.getClass().getSimpleName());
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					refresh();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}, refreshRate, refreshRate, TimeUnit.SECONDS);
		log.info("end   scheduled thread start process. [{}]", this.getClass().getSimpleName());
	}

	private final void stopAutoRefresh() {
		if (executor != null && !executor.isShutdown()) {
			log.info("start scheduled thread shutdown process. [{}]", this.getClass().getSimpleName());
			executor.shutdown();
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				log.warn(e.getMessage(), e);
				log.info("force shutdown scheduled thread.");
				executor.shutdownNow();
			}
			log.info("end   scheduled thread shutdown process. [{}]", this.getClass().getSimpleName());
		}
	}

	@PreDestroy
	public void preDestroy() {
		stopAutoRefresh();
	}
}
