package io.github.laplacedemon.futurenio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.NoSuchElementException;
import java.util.function.Supplier;


public class IOSession {
	private AbstractSelectableChannel asc;
	private ReadBuffer readBuffer;
	private WriteableBufferQueue writeableBufferQueue;
	private Object attr;
	private IOCallback ioCallback;
	private IOCallback nextIOCallback;
	private IOCallback initIOCallback;
	
	public void clean() {
		this.attr = null;
		this.nextIOCallback = null;
		this.ioCallback = initIOCallback;
	}
	
	private IOSession(AbstractSelectableChannel asc) {
		this.asc = asc;
		this.readBuffer = new ReadBuffer();
		this.writeableBufferQueue = new WriteableBufferQueue();
	}
	
	public IOSession(AbstractSelectableChannel asc, IOCallback initIOCallback) {
		this(asc);
		this.ioCallback = initIOCallback;
		this.initIOCallback = initIOCallback;
	}

	public ReadBuffer readBuffer() {
		return this.readBuffer;
	}
	
	public WriteableBufferQueue writeableBufferQueue() {
		return this.writeableBufferQueue;
	}

	public void writeAndFlush(ByteBuffer writeOutByteBuffer) throws NoSuchElementException, IOException {
		// write to buffer
		writeableBufferQueue.push(writeOutByteBuffer);
		flush();
	}
	
	public void flush() throws NoSuchElementException, IOException {
		// write out
		while(true) {
			ByteBuffer lastByteBuffer;
			if(writeableBufferQueue.sizeBlock() > 0) {
				lastByteBuffer = writeableBufferQueue.getLast();
			} else {
				return ;
			}
			
			// 获取Buffer可写的长度。
			int writableBytesLength = lastByteBuffer.remaining();
			
			int writeBytesNum = ((SocketChannel)asc).write(lastByteBuffer);
			if (writeBytesNum < 0) {
				throw new IOException("SocketChannel.write() return negative number.");
			} else {
				// 检查是否全部写完
				if(writeBytesNum == writableBytesLength) {
					writeableBufferQueue.removeLast();
				}
			}
		}
	}

	public Object getAttr() {
		return attr;
	}

	public void setAttr(Object attr) {
		this.attr = attr;
	}

	public Object computeIfAbsentAttr(Supplier<?> su) {
		if(this.attr != null) {
			return this.attr;
		}
		
		Object result = su.get();
		this.attr = result;
		return result;
	}
	
	public void setIOCallback(IOCallback ioCallback) {
		this.ioCallback = ioCallback;
	}
	
	public IOCallback ioCallback() {
		return this.ioCallback;
	}

	public void nextIOCallback(IOCallback ioCallback) {
		this.nextIOCallback = ioCallback;
	}

	public IOCallback nextIOCallback() {
		return nextIOCallback;
	}
	
}
