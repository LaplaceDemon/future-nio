package io.github.laplacedemon.futurenio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.function.Consumer;

public class IOReactor {
	private Selector selector;
	private IOCallback initIOCallback;
	private Consumer<IOSession> connectCallback;
	
	public IOReactor() throws IOException {
		this.selector = Selector.open();
	}
	
	public IOReactor initIOCallback(final IOCallback ioCallback) {
		this.initIOCallback = ioCallback;
		return this;
	}
	
	private void configureNoBlocking(AbstractSelectableChannel asc) throws IOException {
		boolean blocking = asc.isBlocking();
		if (blocking) {
			asc.configureBlocking(false);
		}
	}
	
	public void register(AbstractSelectableChannel asc, int ops) throws IOException {
		configureNoBlocking(asc);
		asc.register(this.selector, ops);
	}
	
	public void register(AbstractSelectableChannel asc, int ops, Object att) throws IOException {
		configureNoBlocking(asc);
		asc.register(this.selector, ops, att);
	}
	
	public void run(Runnable runBefore) throws IOException {
		runBefore.run();
		this.run();
	}
	
	public void run() throws IOException {
		while(true) {
			int keyNum = this.selector.select(1);
//			System.out.println("select:" + keyNum);
			if(keyNum == 0) {
				continue;
			}
			
			final Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				try {
					if(key.isAcceptable()) {
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
						SocketChannel socketChannel = serverSocketChannel.accept();
						handleAcceptable(key, socketChannel);
					} else if (key.isWritable()) {
						SocketChannel socketChannel = (SocketChannel)key.channel();
						handleWritable(key, socketChannel);
					} else if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel)key.channel();
						handleReadable(key, socketChannel);
					} else if (key.isConnectable()) {
						SocketChannel socketChannel = (SocketChannel)key.channel();
						boolean finishConnect = socketChannel.finishConnect();
						if (finishConnect) {
							handleConnectable(key, socketChannel);
						}
					}
				} catch (Exception ex) {
					handleException(key, ex);
				} finally {
					iterator.remove();
				}
			}
		}
	}
	
	private void handleWritable(SelectionKey key, SocketChannel socketChannel) throws IOException {
		// write out
		IOSession ioSession = (IOSession)key.attachment();
		ioSession.flush();
		key.interestOps(SelectionKey.OP_READ);
	}
	
	private void handleReadable(SelectionKey key, SocketChannel socketChannel) throws IOException {
		IOSession ioSession = (IOSession)key.attachment();
		ReadBuffer readBuffer = ioSession.readBuffer();
		int readAllNum = 0;
		while (true) {
			int readNum = readBuffer.readFrom(socketChannel);
			
			// -1 表示连接已结关闭。
			if(readNum < 0) {
				socketChannel.close();
				key.cancel();
				ioSession.ioCallback().closed(ioSession);
				break;
			} else if (readNum == 0) {
				break;
			} else {
				readAllNum += readNum;
			}
		}
		
		// decode
		Object requestMessage = ioSession.ioCallback().decode(ioSession, readBuffer);
		if(requestMessage != null) {
			// handle
			Object responseMessage = ioSession.ioCallback().handle(ioSession, requestMessage);
			// encode
			if(responseMessage != null) {
				ByteBuffer writeOutByteBuffer = ioSession.ioCallback().encode(ioSession, responseMessage);
				// write out
				ioSession.writeAndFlush(writeOutByteBuffer);
			}
		}
		
		if(!key.isValid()) {
			return ;
		}
		
		IOCallback ioCallback = ioSession.nextIOCallback();
		if(ioCallback != null) {
			ioSession.setIOCallback(ioCallback);
			ioSession.nextIOCallback(null);
		}
		
		if(readAllNum == 0) {
			key.interestOps(SelectionKey.OP_WRITE);
		} else {
			key.interestOps(SelectionKey.OP_READ);
		}
		
	}
	
	private void handleConnectable(SelectionKey key, SocketChannel socketChannel) {
		IOSession ioSession = new IOSession(socketChannel, this.initIOCallback);
		key.attach(ioSession);
		this.connectCallback.accept(ioSession);
		
		if(key.isValid()) {
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	private void handleAcceptable(SelectionKey key, SocketChannel socketChannel) throws Exception {
		IOSession ioSession = new IOSession(socketChannel, this.initIOCallback);
		key.attach(ioSession);
		
		// active
		ioSession.ioCallback().active(ioSession);
	}

	private void handleException(SelectionKey key, Exception ex) {
		ex.printStackTrace();
	}

	public void registerConnect(AbstractSelectableChannel asc) throws IOException {
		this.register(asc, SelectionKey.OP_CONNECT);
	}

	public IOReactor connect(Consumer<IOSession> co) {
		this.connectCallback = co;
		return this;
	}
}
