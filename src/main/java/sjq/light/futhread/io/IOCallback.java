package sjq.light.futhread.io;

import java.nio.ByteBuffer;

public interface IOCallback {
	void active(IOSession ioSession);
	
	Object decode(IOSession ioSession, ReadBuffer readBuffer);
	
	Object handle(IOSession ioSession, Object requestMessage);
	
	ByteBuffer encode(IOSession ioSession, Object responseMessage);

//	void connect(IOSession ioSession);
}
