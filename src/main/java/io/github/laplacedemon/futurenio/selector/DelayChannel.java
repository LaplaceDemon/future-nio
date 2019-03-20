package io.github.laplacedemon.futurenio.selector;

public class DelayChannel {
	private long delay;
	private long ticker;
	
	public DelayChannel(int delay, long ticker) {
		this.delay = delay;
		this.ticker = ticker;
	}
	
	public DelayChannel(long delay) {
		this.delay = delay;
		this.ticker = -1;
	}

	public long getDelay() {
		return delay;
	}

	public long getTicker() {
		return ticker;
	}
	
}
