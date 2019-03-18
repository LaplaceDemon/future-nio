package sjq.light.futhread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class Scheduler {
	private static Scheduler instance = new Scheduler();
	
	public static Scheduler getInstance() {
		return instance;
	}
	
	private ForkJoinPool forkJoinPool = new ForkJoinPool();
	
//	public void start() {
//		while(true) {
//			Context taskContext = taskQueue.poll();
//			if(taskContext == null) {
//				break;
//			}
//			
//			StackResult stackResult = taskContext.getStackResult();
//			Consumer<StackResult> runnableTask = taskContext.getTask();
//			runnableTask.accept(stackResult);
//			boolean haxNext = taskContext.goNextTask();
//			if(!haxNext) {
//				continue;
//			}
//			taskQueue.add(taskContext);
//		}
//	}
	
	public void run(final FutureFunction futureFunction) {
		this.forkJoinPool.execute(()->{
			Consumer<StackResult> currentTask = futureFunction.currentTask();
			
			// 运行下一个调度
			currentTask.accept(t);
			Scheduler.this.run(futureFunction);
		});
	}
}
