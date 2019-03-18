package sjq.light.futhread;

import java.util.concurrent.Future;

import org.junit.Test;

public class TestStack {
	
	@Test
	public void stack() {
//		FutureFunction func01 = new FutureFunction().begin((StackResult rs)->{
//			System.out.println(1);
//			System.out.println(2);
//			System.out.println(3);
//			
//			int a = 10;
//			int b = 10;
//			int c = 10;
//			
////			rs.put("a",a);
////			rs.put("b",a);
////			rs.put("c",a);
//		}).yieldThen((StackResult rs)->{
//			System.out.println(4);
//			System.out.println(5);
//			System.out.println(6);
//		});
//		
//		FutureFunction func02 = new FutureFunction().begin((StackResult rs)->{
//			System.out.println(11);
//			System.out.println(12);
//			System.out.println(13);
//		}).yieldThen((StackResult rs)->{
//			System.out.println(14);
//			System.out.println(15);
//			System.out.println(16);
//		});
//		
//		FutureThread futhread01 = new FutureThread(func01);
//		futhread01.start();
//		
//		FutureThread futhread02 = new FutureThread(func02);
//		futhread02.start();
//		
//		Scheduler.getInstance().start();
		
		
		FutureFunction.newBuilder().begin((String str)->{
			
			
			
			return new Integer(1);
		}).then(()->{
			
		})
		
		
		
		
		
		
	}
}
