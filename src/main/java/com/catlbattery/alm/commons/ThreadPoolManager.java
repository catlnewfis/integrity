package com.catlbattery.alm.commons;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadPoolManager {

	private static Log log = LogFactory.getLog(ThreadPoolManager.class);

	private static ThreadPoolManager instance;

	private static final int core_pool_size = 100;

	private static final int maximum_pool_size = 150;

	private static final int keep_alive_time = 30;

	private static final int work_queue_size = 5;

	private static final int task_qos_period = 15;

	private Queue<Runnable> taskQueue = new LinkedList<Runnable>();

	private ThreadPoolManager() {
	}

	public static synchronized ThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new ThreadPoolManager();
		}
		return instance;
	}

	final Runnable accessBufferThread = new Runnable() {

		@Override
		public void run() {
			while (hasMoreAcquire()) {
				log.info("Task Queue Size: " + taskQueue.size());
				threadPool.execute(taskQueue.poll());
			}
		}
	};

	final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
			taskQueue.offer(task);
		}
	};

	ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(core_pool_size, handler);

	final ScheduledFuture<?> taskHandler = scheduler.scheduleAtFixedRate(accessBufferThread, 0, task_qos_period,
			TimeUnit.SECONDS);

	final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(core_pool_size, maximum_pool_size, keep_alive_time,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(work_queue_size), handler);

	private boolean hasMoreAcquire() {
		return !taskQueue.isEmpty();
	}

	public void add(Runnable task) {
		taskQueue.offer(task);
	}

	public void executorTask(Runnable task) {
		if (task != null) {
			threadPool.execute(task);
		}
	}
}
