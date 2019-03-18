package sjq.light.futhread.io;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class WriteableBufferQueue {
	private LinkedList<ByteBuffer> bufferQueue;
	
	public WriteableBufferQueue() {
		this.bufferQueue = new LinkedList<>();
	}

	public void push(ByteBuffer byteBuffer) {
		this.bufferQueue.addFirst(byteBuffer);
	}

	public ByteBuffer getLast() {
		return this.bufferQueue.getLast();
	}
	
	public void removeLast() {
		this.bufferQueue.removeLast();
	}
	
	public int sizeBlock() {
		return this.bufferQueue.size();
	}
}