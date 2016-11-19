package com.net.jsoup;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * �̳߳�
 * 
 * @time 2015��6��25�� ����5:15:29
 * @author CheWenliang
 */
public class ThreadPool {
	private static volatile ThreadPoolExecutor executor = null;

	private ThreadPool() {
	}

	/**
	 * 
	 * @param corePoolSize
	 *            ��С�̳߳�
	 * @param maximumPoolSize
	 *            ����̳߳�
	 * @param aliveTime
	 *            �̴߳��ʱ��
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