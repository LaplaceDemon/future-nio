package sjq.light.futhread.io.http;

import java.nio.ByteBuffer;

import sjq.light.futhread.io.IOCallback;
import sjq.light.futhread.io.IOSession;
import sjq.light.futhread.io.ReadBuffer;

public class HttpContentCallback implements IOCallback {

	@Override
	public void active(IOSession ioSession) {
	}

	@Override
	public Object decode(IOSession ioSession, ReadBuffer readBuffer) {
		HttpParseEntry httpParseEntry = (HttpParseEntry)ioSession.getAttr();
		return httpParseEntry;
	}

	@Override
	public Object handle(IOSession ioSession, Object requestMessage) {
		return null;
	}

	@Override
	public ByteBuffer encode(IOSession ioSession, Object responseMessage) {
		return null;
	}

//	@Override
//	public void connect(IOSession ioSession) {
//	}
}
