package io.github.laplacedemon.futurenio.selector;



import org.junit.Test;

public class TestSelector {
	
	static class Event {
		
	}
	
	@Test
	public void testEmptySelector() throws InterruptedException {
		final Selector selector = new Selector();
		selector.loop();
	}
	
	@Test
	public void testChannel() throws InterruptedException {
		final Selector selector = new Selector();
		
		Channel<Integer> channel = new Channel<>();
		channel.push(1);
		
		selector.register(channel, (Integer e)->{
			System.out.println(e);
		});
		
		selector.loop();
	}
	
	@Test
	public void testThreadsChannel() throws InterruptedException {
		final Selector selector = new Selector();
		
		final Channel<Integer> c1 = new Channel<>();
		final Channel<Integer> c2 = new Channel<>();
		final Channel<Integer> c3 = new Channel<>();
		new Thread(()-> {
			for(;;) {
				c1.push(1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		}).start();
		
		new Thread(()-> {
			for(;;) {
				c2.push(2);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		}).start();
		
		new Thread(()-> {
			for(;;) {
				c3.push(3);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		}).start();
		
		selector.register(c1, (Integer e)->{
			System.out.println(e);
		}).register(c2, (Integer e)->{
			System.out.println(e);
		}).register(c3, (Integer e)->{
			System.out.println(e);
		});
		
		selector.loop();
	}
	
	@Test
	public void testDelayChannel() throws InterruptedException {
		final Selector selector = new Selector();
		
		selector.register(new DelayChannel(1000), ()->{
			System.out.println("1");
		}).register(new DelayChannel(1000), ()->{
			System.out.println("2");
		});
		
		selector.loop();
	}
	
//	public static void main(String[] args) {
//		ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
//		es.scheduleAtFixedRate(() -> {
//			System.out.println("hello");
//		}, 1, 1, TimeUnit.SECONDS);
//	}
}
