package io.github.laplacedemon.futurenio.selector;


public class DelayChannelEntry {
	private DelayChannel ch;
	private Runnable task;
	
	public DelayChannelEntry(DelayChannel ch, Runnable task) {
		super();
		this.ch = ch;
		this.task = task;
	}

	Runnable getTask() {
		return task;
	}

	public DelayChannel channel() {
		return ch;
	}
}
