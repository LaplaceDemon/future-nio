package io.github.laplacedemon.futurenio.promise;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestPromise {
	/*
	public static void main(String[] args) {
		final ScheduledExecutorService se = Executors.newScheduledThreadPool(1);
		Promise p = new Promise((resolve, reject) -> {
			se.schedule(()->{
				resolve(100)
			}, 2000, TimeUnit.MILLISECONDS);
		});
		
		p.then((Object data)->{
			System.out.println(data);
		}, ex -> {
			System.err.println(ex);
		});
	}
	*/
}
