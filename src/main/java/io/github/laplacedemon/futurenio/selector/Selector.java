package io.github.laplacedemon.futurenio.selector;

import java.util.Iterator;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void loop() throws InterruptedException {
		while(true) {
			Iterator<ChannelEntry> iterator = channelList.iterator();
			int n = 0;
			while(iterator.hasNext()) {
				final Object channelEntryObject = iterator.next();
				if(channelEntryObject instanceof ChannelEntry) {
					ChannelEntry channelEntry = (ChannelEntry)channelEntryObject;
					Channel<?> channel = channelEntry.channel();
					while(true) {
						Object msg = channel.poll();
						if(msg != null) {
							n++;
							Consumer consumer = channelEntry.consumer();
							consumer.accept(msg);
						} else {
							break;
						}
					}
				}
			}
			
			DelayTask delayTask = this.delayTaskQueue.poll();
			if(delayTask != null) {
				n++;
				Runnable runnable = delayTask.getRunnable();
				runnable.run();
				long ticker = delayTask.getTicker();
				if(ticker > 0) {
					delayTask.resetTrigger();
					this.delayTaskQueue.put(delayTask);
				}
			}
			
			if(n == 0) {
				synchronized(this) {
					this.wait(1);
				}
			}
		}
	}
	
}
