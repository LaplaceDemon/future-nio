package io.github.laplacedemon.futurenio.http;

import java.nio.ByteBuffer;

import io.github.laplacedemon.futurenio.reactor.IOCallback;
import io.github.laplacedemon.futurenio.reactor.IOSession;
import io.github.laplacedemon.futurenio.reactor.ReadBuffer;

public class HttpContenasdfstCallback implements IOCallback {

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

	@Override
	public void closed(IOSession ioSession) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void connect(IOSession ioSession) {
//	}
}
