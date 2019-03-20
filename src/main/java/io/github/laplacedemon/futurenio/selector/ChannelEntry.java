package io.github.laplacedemon.futurenio.selector;

import java.util.function.Consumer;

public class ChannelEntry {
	private Channel<?> ch;
	
	@SuppressWarnings("rawtypes")
	private Consumer co;
	
	@SuppressWarnings("rawtypes")
	public ChannelEntry(Channel<?> ch, Consumer co) {
		super();
		this.ch = ch;
		this.co = co;
	}

	@SuppressWarnings("rawtypes")
	Consumer consumer() {
		return co;
	}

	public Channel<?> channel() {
		return ch;
	}
	
}
