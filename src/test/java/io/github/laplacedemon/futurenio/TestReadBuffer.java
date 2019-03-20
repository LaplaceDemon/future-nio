package io.github.laplacedemon.futurenio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.laplacedemon.futurenio.reactor.ReadBuffer;

public class TestReadBuffer {
	
	ReadBuffer rb;
	
	@Before
	public void before() throws IOException {
		rb = new ReadBuffer();
		
		ByteArrayInputStream bais = new ByteArrayInputStream("Say:HelloWorld!".getBytes());
		ReadableByteChannel channel = Channels.newChannel(bais);
		
		rb.readFrom(channel);
		
		System.out.println("create a buffer");
	}
	
	@Test
	public void testGetByte() {
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'S');
		}
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'S');
		}
	}
	
	@Test
	public void testGetNBytes() {
		{
			byte[] bs = rb.getNBytes(10);
			String s = new String(bs);
			Assert.assertEquals(s, "Say:HelloW");
		}
		{
			byte[] bs = rb.getNBytes(10);
			String s = new String(bs);
			Assert.assertEquals(s, "Say:HelloW");
		}
	}
	
	@Test
	public void testRead() {
		{
			int b = rb.read();
			Assert.assertEquals((char)b, 'S');
		}
		{
			int b = rb.read();
			Assert.assertEquals((char)b, 'a');
		}
		{
			int b = rb.read();
			Assert.assertEquals((char)b, 'y');
		}
		{
			int b = rb.getByte();
			Assert.assertEquals((char)b, ':');
		}
		{
			int b = rb.getByte();
			Assert.assertEquals((char)b, ':');
		}
		{
			int readableLength = rb.readableLength();
			Assert.assertEquals(readableLength, 12);
		}
	}
	
	@Test
	public void testIndexOf() {
		int index = rb.indexOf((byte)':');
		Assert.assertEquals(index, 3);
	}
	
	@Test
	public void testIndexOfAndGet() {
		int index = rb.indexOf((byte)':');
		Assert.assertEquals(index, 3);
		
		byte b = rb.getByte();
		Assert.assertEquals((char)b, 'S');
	}
	
	@Test
	public void testReadableBytes() {
		int readableLength = rb.readableLength();
		Assert.assertEquals(readableLength, 15);
	}
	
	@Test
	public void testSkip() {
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'S');
		}
		
		// skip 'S'
		rb.skip();
		
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'a');
		}
		
		rb.skip();  // skip 'a'
		rb.skip();  // skip 'y'
		rb.skip();  // skip ':'
		rb.skip();  // skip 'H'
		rb.skip();  // skip 'e'
		
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'l');
		}
		
		rb.skip();  // skip 'l'
		rb.skip();  // skip 'l'
		rb.skip();  // skip 'o'
		rb.skip();  // skip 'W'
		rb.skip();  // skip 'o'
		rb.skip();  // skip 'r'
		rb.skip();  // skip 'l'
		rb.skip();  // skip 'd'
		
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, '!');
		}
		
		rb.skip();  // skip '!'
		
		try {
			rb.skip();  // skip ''
		} catch (NoSuchElementException ex) {
			Assert.assertEquals(ex.getClass(), NoSuchElementException.class);
		}
		
	}
	
	@Test
	public void testSkipN() {
		{
			byte b = rb.getByte();
			Assert.assertEquals((char)b, 'S');
		}
		
		// skip 'S'
		rb.skip(10);  // skip "Say:HelloW"
		{
			byte b = rb.getByte();
//			System.out.println((char)b);
			Assert.assertEquals((char)b, 'o');
		}
		
		int readableLength = rb.readableLength();
//		System.out.println(readableLength);
		Assert.assertEquals(readableLength, 5);
	}
	
}
