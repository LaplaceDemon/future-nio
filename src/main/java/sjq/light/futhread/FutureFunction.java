package sjq.light.futhread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FutureFunction {
	
	private Queue<Object> taskQueue;
	
	public static class Builder {
		private Queue<Object> taskQueue = new LinkedList<>();
		
		public <T> Builder begin(Function<T, Future<?>> fun) {
			taskQueue.add(fun);
			return this;
		}
		
		public <T> Builder then(Function<T, Future<?>> fun) {
			taskQueue.add(fun);
			return this;
		}
		
		public Builder exception(Function<? extends Exception, Future<?>> fun) {
			taskQueue.add(fun);
			return this;
		}
		
		public FutureFunction complete() {
			FutureFunction futureFunction = new FutureFunction();
			futureFunction.taskQueue = this.taskQueue;
			return futureFunction;
		}
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	public Object curretTask(int runTaskIndex) {
		Object task = this.taskQueue.poll();
		return task;
	}
	
	public void 
	
}
