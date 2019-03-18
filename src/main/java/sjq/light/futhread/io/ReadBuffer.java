package sjq.light.futhread.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * <pre>
 * write in
 *    ↓
 * [Buffer] <--> [Buffer] <--> [Buffer]
 *                                 ↓
 *                             read out
 * <pre>         
 * @author jackie.sjq
 *
 */
public class ReadBuffer {
	/**
	 * 可读长度
	 */
	private int readableLength;
	
	private LinkedList<ByteBuffer> byteBufferQueue = new LinkedList<>();
	
	/*
	private void push(ByteBuffer buffer) {
		buffer.flip();
		readableLength += buffer.limit();
		this.byteBufferQueue.push(buffer);
	}
	*/
	
	/**
	 * 复制最后字节，但是不消费掉。
	 * @return
	 */
	public byte getByte() {
		ByteBuffer lastByteBuffer;
		while(true) {
			try {
				lastByteBuffer = this.byteBufferQueue.getLast();
			} catch (NoSuchElementException e) {
				// 没有数据了。
				throw e;
			}
			
			if(lastByteBuffer.remaining() == 0) {
				this.byteBufferQueue.removeLast();
				continue;
			}
			
			lastByteBuffer.mark();
			byte b = lastByteBuffer.get();
			lastByteBuffer.reset();
			return b;
		}
	}
	
	public int indexOf(byte b) {
		int index = 0;
		Iterator<ByteBuffer> descendingIterator = this.byteBufferQueue.descendingIterator();
		ByteBuffer currentBuffer;
		while(descendingIterator.hasNext()) {
			currentBuffer = descendingIterator.next();
			currentBuffer.mark();
			
			// 剩余字节长度
			for (int i = currentBuffer.position(); i < currentBuffer.limit(); i++) {
				byte cb = currentBuffer.get(i);
				if (cb == b) {
					currentBuffer.reset();
					return index;
				}
				index++;
			}
			
			currentBuffer.reset();
			index += currentBuffer.remaining();
		}
		
		return -1;
	}
	
	public int indexOf(byte... bs) {
		int index = 0;
		Iterator<ByteBuffer> descendingIterator = this.byteBufferQueue.descendingIterator();
		while(descendingIterator.hasNext()) {
			ByteBuffer currentBuffer = descendingIterator.next();
			currentBuffer.mark();
			
			// 剩余字节长度
			int remainingBufferLength = currentBuffer.remaining();
			for (int i = 0; i < remainingBufferLength; i++) {
				byte cb = currentBuffer.get(i);
				if (cb == bs[0]) {
					currentBuffer.reset();
					return index;
				}
				index++;
			}
			
			currentBuffer.reset();
			index += remainingBufferLength;
		}
		
		return -1;
	}
	
	/**
	 * 复制最后N个字节，但不消费掉。
	 * @return
	 */
	public byte[] getNBytes(int needLength) {
		byte[] bs = new byte[needLength];
		int readLen = 0;
		int needReadLength = needLength;
		Iterator<ByteBuffer> descendingIterator = this.byteBufferQueue.descendingIterator();
		while(descendingIterator.hasNext()) {
			ByteBuffer nextBuffer = descendingIterator.next();
			int remainingNextBufferLength = nextBuffer.remaining();
			nextBuffer.mark();
			if(needReadLength <= remainingNextBufferLength) {
				// 需要读的长度比较小，那么读取需要的字节既可。
				nextBuffer.get(bs, readLen, needReadLength);
				readLen += needReadLength;
				needReadLength -= needReadLength;
			} else {
				// 需要读的长度比较多，那么当前buffer的全部剩余字节。
				nextBuffer.get(bs, readLen, remainingNextBufferLength);
				readLen += remainingNextBufferLength;
				needReadLength -= remainingNextBufferLength;
			}
			
			nextBuffer.reset();
			
			if(needReadLength == 0) {
				break;
			}
			
		}
		return bs;
	}


	public byte[] readNBytes(int needLength) {
		byte[] bs = new byte[needLength];
		int readlen = 0;
		int unReadNeedLength = needLength;
		while(true) {
			// 最后一个Buffer
			ByteBuffer lastByteBuffer;
			try {
			    lastByteBuffer = this.byteBufferQueue.getLast();
			} catch (NoSuchElementException e) {
				throw e;
			}
			
			// ByteBuffer剩余长度
//			int len = lastByteBuffer.limit() - lastByteBuffer.position();
			int remaining = lastByteBuffer.remaining();
			if(remaining == 0) {
				this.byteBufferQueue.removeLast();
				continue;
			}
			
			if(unReadNeedLength < remaining) {
				lastByteBuffer.get(bs,readlen,unReadNeedLength);
				readlen += unReadNeedLength;
				unReadNeedLength -= unReadNeedLength;
				break;
			} else {
				lastByteBuffer.get(bs,readlen,remaining);
				readlen += remaining;
				unReadNeedLength -= remaining;
			}
			
			if(readlen == needLength) {
				break;
			}
		}
		
		this.readableLength -= needLength;
		return bs;
	}

	public int readableLength() {
		return this.readableLength;
	}

	public int read() {
		while(true) {
			// 最后一个Buffer
			ByteBuffer lastByteBuffer = this.byteBufferQueue.getLast();
			if(lastByteBuffer == null) {
				throw new RuntimeException();
			}
			
			// ByteBuffer剩余长度
			int len = lastByteBuffer.limit() - lastByteBuffer.position();
			if(len == 0) {
				this.byteBufferQueue.removeLast();
				continue;
			}
			
			byte b = lastByteBuffer.get();
			
			this.readableLength -= 1;
			return b;
		}
		
	}

	public byte readByte() {
		return (byte)read();
	}
	
	public void skip() {
		ByteBuffer lastByteBuffer;
		while(true) {
			lastByteBuffer = this.byteBufferQueue.getLast();
			
			if(lastByteBuffer.remaining() == 0) {
				this.byteBufferQueue.removeLast();
				continue;
			}
			
			int position = lastByteBuffer.position();
			lastByteBuffer.position(position + 1);
			
			this.readableLength--;
			return ;
		}
		
	}
	
	public void skip(int skipN) {
		if(skipN > this.readableLength) {
			throw new IllegalArgumentException("The readable bytes are not enough");
		}
		
		if(skipN < 0) {
			throw new IllegalArgumentException("the argument must been greater than 0.");
		}
		ByteBuffer lastByteBuffer;
		while(true) {
			if(skipN == 0) {
				return ;
			}
			lastByteBuffer = this.byteBufferQueue.getLast();
			
			int remainingLength = lastByteBuffer.remaining();
			if (remainingLength == 0) {
				this.byteBufferQueue.removeLast();
				continue;
			} else if (remainingLength <= skipN) {
				int position = lastByteBuffer.position();
				lastByteBuffer.position(position + remainingLength);
				skipN -= remainingLength;
				this.readableLength -= remainingLength;
				this.byteBufferQueue.removeLast();
				continue;
			} else if (remainingLength > skipN) {
				int position = lastByteBuffer.position();
				lastByteBuffer.position(position + skipN);
				this.readableLength -= skipN;
				skipN = 0;
			}
		}
	}

	public void clear() {
		this.byteBufferQueue.clear();
	}

	public int readFrom(SocketChannel socketChannel) throws IOException {
		ByteBuffer lastBuffer;
		if(this.byteBufferQueue.size() == 0) {
			// writable mode buffer
			ByteBuffer wb = this.createByteBuffer();
			this.byteBufferQueue.addFirst(wb);
			lastBuffer = wb;
		} else {
			// Readable Buffer
			ByteBuffer r2wBuffer = this.byteBufferQueue.getFirst();
			r2wBuffer.position(r2wBuffer.limit());
			r2wBuffer.limit(r2wBuffer.capacity());
			lastBuffer = r2wBuffer;
		}
		
		int remaining = lastBuffer.remaining();
		if (remaining == 0) {
			lastBuffer.flip();
			ByteBuffer wb = this.createByteBuffer();
			this.byteBufferQueue.addFirst(wb);
			lastBuffer = wb;
		}
		
		int readLength = socketChannel.read(lastBuffer);
		if(readLength == -1) {
			lastBuffer.flip();
			return readLength;
		}
		
		this.readableLength += readLength;
		lastBuffer.flip();  // writable to readable
		return readLength;
	}
	
	public ByteBuffer createByteBuffer() {
		// writable mode buffer
		return ByteBuffer.allocate(1024);
	}
	
}
