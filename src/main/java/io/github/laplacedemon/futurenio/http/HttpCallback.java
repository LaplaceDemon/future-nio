package io.github.laplacedemon.futurenio.http;

import java.nio.ByteBuffer;

import io.github.laplacedemon.futurenio.IOCallback;
import io.github.laplacedemon.futurenio.IOSession;
import io.github.laplacedemon.futurenio.ReadBuffer;
import io.github.laplacedemon.futurenio.http.HttpParseEntry.HttpContentParseWay;

public class HttpCallback implements IOCallback {

	@Override
	public Object handle(IOSession ioSession, Object requestMessage) {
		HttpParseEntry httpParseEntry = (HttpParseEntry)requestMessage;
		byte[] readHttpContent = httpParseEntry.getHttpContent();
		System.out.println(new String(readHttpContent));
		ioSession.clean();
		return null;
	}
	
	@Override
	public Object decode(IOSession ioSession, ReadBuffer readBuffer) {
		HttpParseEntry httpParseEntry = (HttpParseEntry)ioSession.computeIfAbsentAttr(()->{
			return new HttpParseEntry();
		});
		
		String unresolvedString = httpParseEntry.getUnresolvedCompletedString();
		
		int readableLength = readBuffer.readableLength();
		byte[] readNBytes = readBuffer.readNBytes(readableLength);
		String str = new String(readNBytes);
		
		int index = 0;
		if(unresolvedString != null) {
			int indexOf = str.indexOf("\r\n", index);
			if(indexOf == -1) {
				httpParseEntry.setUnresolvedCompletedString(unresolvedString + str);
				return null;
			} else if (indexOf-index == 0) {
				// System.out.println("开始解析content");
				String unresolved = str.substring(index);
				httpParseEntry.setUnresolvedCompletedString(unresolved);
//				ioSession.nextIOCallback(new HttpContentCallback());
				return null;
			}
			
			String line = unresolvedString + str.substring(index, indexOf);
			httpParseEntry.setUnresolvedCompletedString(null);
			handleHttpObject(line, httpParseEntry);
		}
		
		while (true) {
			int indexOf = str.indexOf("\r\n", index);
			if(indexOf == -1) {
				String unresolved = str.substring(index);
				httpParseEntry.setUnresolvedCompletedString(unresolved);
				break;
			} else if (indexOf - index == 0) {
				// 开始解析content
				System.out.println("开始解析content");
				String unresolved = str.substring(index);
				httpParseEntry.setUnresolvedCompletedString(unresolved);
				return httpParseEntry;
			}
			String line = str.substring(index, indexOf);
			index = indexOf + "\r\n".length();
			handleHttpObject(line, httpParseEntry);
		}
		
		return null;
	}
	
	private void handleHttpObject(String httpline, HttpParseEntry httpParseEntry) {
		if(httpline.startsWith("HTTP")) {
			return ;
		}
		String[] headerStr = httpline.split(": ");
		String headerKey = headerStr[0];
		String headerValue = headerStr[1];
		
		if(headerKey.equalsIgnoreCase("Transfer-Encoding") && headerValue.equalsIgnoreCase("chunked")) {
			httpParseEntry.setParseWay(HttpContentParseWay.Chunked);
		} else if (headerKey.equalsIgnoreCase("Content-Length")) {
			int contentLength = Integer.valueOf(headerValue);
			httpParseEntry.setParseWay(HttpContentParseWay.ContentLength);
			httpParseEntry.setContentLength(contentLength);
		}
		
		httpParseEntry.add(headerKey, headerValue);
	}
	
	@Override
	public ByteBuffer encode(IOSession ioSession, Object responseMessage) {
		return null;
	}
	
	@Override
	public void active(IOSession ioSession) {
		System.out.println("new channel, active!");
	}

	@Override
	public void closed(IOSession ioSession) {
		System.out.println("远端连接已关闭");
	}

}
