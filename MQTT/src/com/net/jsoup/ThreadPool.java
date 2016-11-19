package com.net.jsoup;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * 
 * @time 2015年6月25日 下午5:15:29
 * @author CheWenliang
 */
public class ThreadPool {
	private static volatile ThreadPoolExecutor executor = null;

	private ThreadPool() {
	}

	/**
	 * 
	 * @param corePoolSize
	 *            最小线程池
	 * @param maximumPoolSize
	 *            最大线程池
	 * @param aliveTime
	 *            线程存活时间
	 * @param unit
	 * @return
	 */
	public static ThreadPoolExecutor getInstance(int corePoolSize,
			int maximumPoolSize, long aliveTime, TimeUnit unit) {
		if (executor == null) {
			synchronized (ThreadPool.class) {
				BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
				executor = new ThreadPoolExecutor(corePoolSize,
						maximumPoolSize, aliveTime, unit, queue);
			}
		}

		return executor;
	}

	public static ThreadPoolExecutor getInstance() {
		if (executor == null) {
			executor = getInstance(3, 5, 10, TimeUnit.SECONDS);
		}

		return executor;
	}

}