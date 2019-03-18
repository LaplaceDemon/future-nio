package sjq.light.futhread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

import sjq.light.futhread.io.IOCallback;
import sjq.light.futhread.io.IOReactor;
import sjq.light.futhread.io.IOSession;
import sjq.light.futhread.io.ReadBuffer;
import sjq.light.futhread.io.WriteableBufferQueue;

public class TestHttp {
	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		SocketAddress adds = new InetSocketAddress(10000);
		ssc.bind(adds);
		
		final IOReactor reactor = new IOReactor();
		reactor.setIOCallback(new IOCallback() {
			
			
		});
		
		reactor.register(ssc, SelectionKey.OP_ACCEPT);
	}
}
