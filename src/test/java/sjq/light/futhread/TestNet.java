package sjq.light.futhread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.Test;

public class TestNet {
	
	FutureFunction ff = new FutureFunction().begin((StackResult sr)->{
		ByteBuffer dst = ByteBuffer.allocate(100);
		sr.put("dst", dst);
	}).thenWhile((StackResult sr)->{
		SocketChannel sc = (SocketChannel)sr.getArg0();
		Selector sel = (Selector)sr.getArg1();
		ByteBuffer dst = (ByteBuffer)sr.get("dst");
		int len = sc.read(dst);
		if(len <= 0) {
			sc.register(sel, SelectionKey.OP_READ);
			sr.put("len", len);
			sr.waitContinue();
			return ;
		}
	}).then((StackResult sr)->{
		int len = (Integer)sr.get("len");
		if(len < 0) {
			
			return;
		}
		
		// 读到了数据
		
		// 处理一下数据
		
		// 写回数据
	}).thenWhile((StackResult sr)->{
		SocketChannel sc = (SocketChannel)sr.getArg0();
		Selector sel = (Selector)sr.getArg1();
		ByteBuffer dst = (ByteBuffer)sr.get("dst");
		int len = sc.write(dst);
		if(len <= 0) {
			sc.register(sel, SelectionKey.OP_READ);
			sr.put("len", len);
			sr.waitContinue();
			return ;
		}
	}).then(()->{
		
	});
	
	@Test
	public void server() throws IOException {
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		/*
		
		Selector selector = Selector.open();
		for(;;) {
			int num = selector.select(100);
			if(num <= 0) {
				continue;
			}
			
			Set<SelectionKey> keys = selector.keys();
			for(SelectionKey key : keys) {
				if() {
					
				}
			}
		}
		*/
		for(;;) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			
			FutureThread fth = new FutureThread(ff, socketChannel);
		}
		
	}
}
