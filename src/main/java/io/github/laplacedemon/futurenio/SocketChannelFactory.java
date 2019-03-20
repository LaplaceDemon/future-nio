package io.github.laplacedemon.futurenio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class SocketChannelFactory {
	
	private SocketChannelFactory() {}
	
	public static SocketChannel createNewConnection(final String hostname, final int port) throws IOException {
		SocketAddress saddr = new InetSocketAddress(hostname, port);
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		boolean connect = socketChannel.connect(saddr);
		return socketChannel;
	}
	
}
