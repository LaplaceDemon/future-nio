package sjq.light.futhread;

import org.junit.Test;

public class TestFutureFunction {
	
	@Test
	public void build() {
		FutureFunction func = new FutureFunction().begin((StackResult rs)->{
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
		}).yieldThen((StackResult rs)->{
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
		});
		
		
		FutureFunction futureFunction0 = FutureFunction.newBuilder()
			.begin(()->{
				System.out.println(1);
				System.out.println(2);
				System.out.println(3);
			}).then(()->{
				System.out.println(4);
				System.out.println(5);
				System.out.println(6);
			}).complate();
		
		FutureFunction futureFunction1 = FutureFunction.newBuilder()
				.begin(()->{
					System.out.println(7);
					System.out.println(8);
					System.out.println(9);
				}).then(()->{
					System.out.println(10);
					System.out.println(11);
					System.out.println(12);
				}).complate();
		
		
		Scheduler scheduler = new Scheduler();
		scheduler.run(futureFunction0);
		scheduler.run(futureFunction1);
	
	}

}
