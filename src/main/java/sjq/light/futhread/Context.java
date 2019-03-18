package sjq.light.futhread;

import java.util.function.Consumer;

public class Context {
	private FutureFunction func;
	private int runTaskIndex;
	private StackResult stackResult;
	
	public Context(FutureFunction func) {
		this.func = func;
		this.runTaskIndex = 0;
		this.stackResult = new StackResult();
	}

	public Context(FutureFunction func, Object... args) {
		this.func = func;
		this.runTaskIndex = 0;
		this.stackResult = new StackResult();
		for(Object arg : args) {
			stackResult.setArg0(arg0);
		}
	}

	public Consumer<StackResult> getTask() {
		Consumer<StackResult> runnable = func.at(runTaskIndex);
		return runnable;
	}

	public boolean goNextTask() {
		runTaskIndex++;
		if(runTaskIndex >= func.taskSize()) {
			return false;
		}
		
		return true;
	}

	public StackResult getStackResult() {
		return stackResult;
	}
}
