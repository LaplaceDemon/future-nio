package sjq.light.futhread.io.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

import sjq.light.futhread.io.IOReactor;
import sjq.light.futhread.io.IOSession;
import sjq.light.futhread.io.SocketChannelFactory;

public class AsyncHttpClient {
	public static void main(String[] args) throws IOException {
		final IOReactor ioReactor = new IOReactor();
//		final IOReactor ioWorkReactor = new IOReactor();
		
//		ioWorkReactor.run();
		
		// connect
		final SocketChannel sc = SocketChannelFactory.createNewConnection("www.baidu.com", 80);
		ioReactor.registerConnect(sc);
		
		Runtime.getRuntime().addShutdownHook(new Thread(()-> {
			try {
				System.out.println("关闭连接");
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
		
		ioReactor.initIOCallback(new HttpHeaderCallback()).connect((IOSession ios) -> {
			
			System.out.println("new channel, connect!");
			
			String s = "GET / HTTP/1.1\r\n";
				  s += "Host: www.baidu.com\r\n";
				  s += "User-Agent: curl/7.55.1\r\n";
				  s += "Accept: */*\r\n";
				  s += "\r\n";
				  
			ByteBuffer bb = ByteBuffer.wrap(s.getBytes());
			try {
				ios.writeAndFlush(bb);
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}).run(()->{
			System.out.println("the reactor is running.");
		});
	}
	
}
