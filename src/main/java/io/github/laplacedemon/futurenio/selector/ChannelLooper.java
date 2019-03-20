package io.github.laplacedemon.futurenio.selector;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;

public class ChannelLooper {
	private final Selector selector;
	
    public ChannelLooper(Selector selector) {
    	this.selector = selector;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void loopAtCurrentThread() throws InterruptedException {
		while(true) {
			Iterator<ChannelEntry> iterator = selector.getChannelList().iterator();
			int n = 0;
			while(iterator.hasNext()) {
				final Object channelEntryObject = iterator.next();
				if(channelEntryObject instanceof ChannelEntry) {
					ChannelEntry channelEntry = (ChannelEntry)channelEntryObject;
					Channel<?> channel = channelEntry.channel();
//					while(true) {
//						Object msg = channel.poll();
//						if(msg != null) {
//							n++;
//							Consumer consumer = channelEntry.consumer();
//							consumer.accept(msg);
//						} else {
//							break;
//						}
//					}
					
					Object msg = channel.poll();
					if(msg != null) {
						++n;
						Consumer consumer = channelEntry.consumer();
						consumer.accept(msg);
					}
				}
			}
			
			DelayQueue<DelayTask> delayTaskQueue = selector.getDelayTaskQueue();
			DelayTask delayTask = delayTaskQueue.poll();
			if(delayTask != null) {
				++n;
				Runnable runnable = delayTask.getRunnable();
				runnable.run();
				long ticker = delayTask.getTicker();
				if(ticker > 0) {
					delayTask.resetTrigger();
					delayTaskQueue.put(delayTask);
				}
			}
			
			if(n == 0) {
				synchronized(selector) {
					this.wait(1);
				}
			}
		}
	}
	
	public void loop() throws InterruptedException {
		loopAtCurrentThread();
	}
}
