package io.github.laplacedemon.futurenio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

public class TestMain {
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		Selector selector = Selector.open();
		
		socketChannel.configureBlocking(true);
		socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
		socketChannel.finishConnect();
		
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		
		while(true) {
			int keyNum = selector.select(10000);
			System.out.println(new Date() + ":select keyNum:" + keyNum);
			
			final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				if(key.isConnectable()) {
					SocketChannel sc = (SocketChannel)key.channel();
					boolean finishConnect = sc.finishConnect();
					if (finishConnect) {
						System.out.println("connected");
					}
				} else if(key.isReadable()) {
					System.out.println("read");
				} else if(key.isWritable()) {
					System.out.println("write");
				} else if(key.isAcceptable()) {
					System.out.println("accept");
				}
			}
		}
	}
}
