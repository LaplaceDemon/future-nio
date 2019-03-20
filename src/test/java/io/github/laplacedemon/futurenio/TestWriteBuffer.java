package io.github.laplacedemon.futurenio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.Before;
import org.junit.Test;

import io.github.laplacedemon.futurenio.WriteableBufferQueue;

public class TestWriteBuffer {
	WriteableBufferQueue wbq;
	Path path;
	
	@Before
	public void before() throws IOException {
//		path = Paths.get("e:/filedir/testfile2019.txt");
		
		File testFile = File.createTempFile("test-java", ".dat");
		System.out.println("create a tmp file:" + testFile.getAbsolutePath());
		path = testFile.toPath();
		
		
		wbq = new WriteableBufferQueue();
		
		ByteBuffer b1 = ByteBuffer.allocate(10);
		b1.put("Say:".getBytes());
		wbq.push(b1);
		
		ByteBuffer b2 = ByteBuffer.allocate(10);
		b2.put("HelloWorld".getBytes());
		wbq.push(b2);
		
		ByteBuffer b3 = ByteBuffer.allocate(10);
		b3.put("!".getBytes());
		wbq.push(b3);
		
		System.out.println("create a buffer.");
		
		if (testFile.exists()) {
			testFile.delete();
		}
		
		System.out.println("clean the old test file.");
	}
	
	
	@Test
	public void testWrite() throws IOException {
		FileChannel ch = FileChannel.open(path, StandardOpenOption.CREATE ,StandardOpenOption.WRITE);
		
		ByteBuffer lastByteBuffer = wbq.getLast();
		
		if(needFlip(lastByteBuffer)) {
			System.out.println("flip last byteBuffer");
			lastByteBuffer.flip();
		}
		
		int write = ch.write(lastByteBuffer);
		System.out.println("Write byte count:" + write);
	}
	
	@Test
	public void testWrapWrite() throws IOException {
		FileChannel ch = FileChannel.open(path, StandardOpenOption.CREATE ,StandardOpenOption.WRITE);
		
		ByteBuffer lastByteBuffer = ByteBuffer.wrap("Say2:".getBytes());
		
		int write = ch.write(lastByteBuffer);
		System.out.println("Write byte count:" + write);
	}
	
	
	public boolean needFlip(ByteBuffer bb) {
		if(bb.limit() == bb.position() && bb.position() == 0) {
			return false;
		}
		return true;
	}
}
