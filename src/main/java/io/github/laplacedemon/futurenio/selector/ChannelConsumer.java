package io.github.laplacedemon.futurenio.selector;

@FunctionalInterface
public interface ChannelConsumer<T> {
	void accept(T obj);
}
