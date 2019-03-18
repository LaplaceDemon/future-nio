package sjq.light.futhread;

import org.junit.Test;

public class TestFutureThread {
	
	@Test
	public void start() {
		FutureFunction func01 = new FutureFunction().begin((StackResult rs)->{
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
		}).yieldThen((StackResult rs)->{
			System.out.println(4);
			System.out.println(5);
			System.out.println(6);
		});
		
		FutureFunction func02 = new FutureFunction().begin((StackResult rs)->{
			System.out.println(11);
			System.out.println(12);
			System.out.println(13);
		}).yieldThen((StackResult rs)->{
			System.out.println(14);
			System.out.println(15);
			System.out.println(16);
		});
		
		FutureThread futhread01 = new FutureThread(func01);
		futhread01.start();
		
		FutureThread futhread02 = new FutureThread(func02);
		futhread02.start();
		
		Scheduler.getInstance().start();
	}
	
	
}
