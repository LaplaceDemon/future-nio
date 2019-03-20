package io.github.laplacedemon.futurenio.selector;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Channel<T> {
	private BlockingQueue<T> blockQueue;
	private Selector selector;
	
	private AtomicBoolean selectorBlocking;
	
	public Channel() {
		this.blockQueue = new LinkedBlockingQueue<>();
	}
	
	public void push(T msg) {
		boolean add = this.blockQueue.add(msg);
		if(add && selectorBlocking != null) {
			if(selectorBlocking.get()) {
				selector.notify();
			}
		}
	}
	
	public T poll() {
		return blockQueue.poll();
	}

	void setSelectorBlockingStatus(AtomicBoolean blocking) {
		this.selectorBlocking = blocking;
	}
	
}
