package io.github.laplacedemon.futurenio.promise;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Promise {
	/*
	private PromiseStatus status = PromiseStatus.Pending;
//	private Runnable runTask;
	private Object value;
	private Object reason;
	private List<Object> onFulfilledArr;
	private List<Object> onRejectedArr;
	
	public Promise(BiConsumer<Consumer<Object>, Consumer<Object>> executor) {
		try {
			executor.accept(this::resolve, this::reject);
		} catch (Exception e) {
			reject(e);
		}
	}
	
	public Promise then(Consumer<Object> onFulfilled, Consumer<Object> onRejected) {
		if(this.status.equals(PromiseStatus.Fulfilled)) {
			onFulfilled.accept(this.value);
		} else if(this.status.equals(PromiseStatus.Rejected)) {
			onRejected.accept(this.reason);
		} else if(this.status.equals(PromiseStatus.Pending)) {
			this.onFulfilledArr.add(onFulfilled);
			this.onRejectedArr.add(onRejected);
		}
	}
	
	private void resolve(Object value) {
		if(this.status.equals(PromiseStatus.Pending)) {
			this.value = value;
			this.status = PromiseStatus.Fulfilled;
			for(Object fn:this.onFulfilledArr) {
				fn(this.value);
			}
		}
	}
	
	private void reject(Object reason) {
		if(this.status.equals(PromiseStatus.Pending)) {
			this.reason = reason;
			this.status = PromiseStatus.Rejected;
			for(Object fn:this.onRejectedArr) {
				fn(this.reason);
			}
		}
	}
	*/
}
