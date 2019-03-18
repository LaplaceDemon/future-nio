package sjq.light.futhread.io.http;

import java.util.HashMap;
import java.util.Map;

import sjq.light.futhread.io.ReadBuffer;

public class HttpParseEntry {
	public static enum HttpContentParseWay {
		ContentLength,Chunked;
	}
	
	private HttpContentParseWay parseWay;
	private String unresolvedCompletedString;
//	private int resolvedIndex;
	private Map<String,String> headerMap;
//	private byte[] content;
	private int contentLength;
	
	public HttpParseEntry() {
		headerMap = new HashMap<>();
	}
	
	public String getUnresolvedCompletedString() {
		return unresolvedCompletedString;
	}
	
	public void setUnresolvedCompletedString(String unresolvedCompletedString) {
		this.unresolvedCompletedString = unresolvedCompletedString;
	}
	
	public byte[] readHttpContent(ReadBuffer readBuffer) {
		if(this.parseWay.equals(HttpContentParseWay.ContentLength)) {
			if (this.contentLength <= readBuffer.readableLength()) {
				byte[] readNBytes = readBuffer.readNBytes(this.contentLength);
				return readNBytes;
			}
		}
		
		return null;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	
	public void add(String headKey,String headValue) {
		this.headerMap.put(headKey, headValue);
	}
	
	public void setParseWay(HttpContentParseWay parseWay) {
		this.parseWay = parseWay;
	}

	public HttpContentParseWay getParseWay() {
		return parseWay;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getContentLength() {
		return contentLength;
	}

	public byte[] getHttpContent() {
		return unresolvedCompletedString.getBytes();
	}

}
