package io.github.laplacedemon.futurenio.selector;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTask implements Delayed {
	private long trigger;
	private final long ticker;
	private Runnable runnable;
	
	public DelayTask(long delay, long ticker, Runnable runnable) {
		this.trigger=System.nanoTime() + TimeUnit.NANOSECONDS.convert(delay, TimeUnit.MILLISECONDS);
		this.ticker = ticker;
		this.runnable = runnable;
	}

	@Override
	public int compareTo(Delayed o) {
		DelayTask that = (DelayTask)o;
		if(this.trigger > that.trigger) {
			return 1;
		} else if(this.trigger < that.trigger) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger-System.nanoTime(), java.util.concurrent.TimeUnit.NANOSECONDS);
	}

	public long getTicker() {
		return ticker;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public void resetTrigger() {
		this.trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(this.ticker, TimeUnit.MILLISECONDS);
	}
	
}
