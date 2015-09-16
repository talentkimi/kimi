package core.lang.linux;

import java.io.InputStream;
import java.math.BigDecimal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.text.StringDigestor;

public class LoadAverageRunnable implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(LoadAverageRunnable.class);


	private static final LoadAverageRunnable SINGLETON = new LoadAverageRunnable();

	public static final LoadAverageRunnable getLoadAverageRunnable() {
		return SINGLETON;
	}

	private volatile double loadAverage = 0.0;

	public double getLoadAverage() {
		return loadAverage;
	}

	public void waitForLoadAverage(double maxLoadAverage) {
		if (maxLoadAverage < 0.5) {
			if (log.isDebugEnabled()) log.debug ("[Warning] Load Average Threshold TOO LOW: " + maxLoadAverage);
			return;
		}
		if (loadAverage > maxLoadAverage) {
			if (log.isDebugEnabled()) log.debug ("[Thread Sleeping] " + Thread.currentThread().getId() + ":" + Thread.currentThread().getName());
			while (loadAverage > maxLoadAverage) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			if (log.isDebugEnabled()) log.debug ("[Thread Awake] " + Thread.currentThread().getId() + ":" + Thread.currentThread().getName());
		}
	}

	public void waitForLoadAverage() {
		waitForLoadAverage(0.8);
	}

	private LoadAverageRunnable() {
		new Thread(this, "LoadAverageRunnable").start();
	}

	private final String uptime() throws Throwable {
		Process process = Runtime.getRuntime().exec("uptime");
		process.waitFor();
		InputStream input = process.getInputStream();
		StringBuilder response = new StringBuilder();
		while (true) {
			int c = input.read();
			if (c == -1) {
				break;
			}
			response.append((char) c);
		}

		return response.toString();
	}

	public void run() {
		try {
			uptime();
			while (true) {
				try {
					String response = uptime();
					StringDigestor digestor = new StringDigestor(response);
					digestor.find("load average:", true);
					digestor.whitespace();
					loadAverage = new BigDecimal(digestor.decimal()).doubleValue();
					if (log.isDebugEnabled()) log.debug ("[Load Average] " + loadAverage);
					Thread.sleep(1000);
				} catch (Throwable t) {
					if (log.isDebugEnabled()) log.debug ("exception", t);
				}
			}
		} catch (Throwable t) {
			if (log.isDebugEnabled()) log.debug ("[Load Average Disabled]", t);
		}
	}

}
