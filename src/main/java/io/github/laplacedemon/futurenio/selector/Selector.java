package io.github.laplacedemon.futurenio.selector;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Selector {
	private final CopyOnWriteArrayList<ChannelEntry> channelList;
    private final AtomicBoolean blocking;
    private final DelayQueue<DelayTask> delayTaskQueue;
	
	public Selector() {
		this.channelList = new CopyOnWriteArrayList<>();
		this.delayTaskQueue = new DelayQueue<>();
		this.blocking = new AtomicBoolean(false);
	}
	
	public <T> Selector register(Channel<T> channel, Consumer<T> co) {
		channel.setSelectorBlockingStatus(this.blocking);
		ChannelEntry channelEntry = new ChannelEntry(channel, co);
		this.channelList.add(channelEntry);
		return this;
	}
	
	public Selector register(DelayChannel delayChannel, Runnable runnable) {
		DelayChannelEntry channelEntry = new DelayChannelEntry(delayChannel, runnable);
		DelayChannel channel = channelEntry.channel();
		long delay = channel.getDelay();
		long ticker = channel.getTicker();
		DelayTask delayTask = new DelayTask(delay, ticker, channelEntry.getTask());
		this.delayTaskQueue.put(delayTask);
		return this;
	}
	
	public Selector registerException(Consumer<Exception> exco) {
		return this;
	}

	public CopyOnWriteArrayList<ChannelEntry> getChannelList() {
		return channelList;
	}

	public DelayQueue<DelayTask> getDelayTaskQueue() {
		return delayTaskQueue;
	}
	
	public ChannelLooper makeLooper() {
		return new ChannelLooper(this);
	}
	
}
