package sjq.light.futhread;

public class FutureThread {
	private FutureFunction func;
	private Object[] args;
	
	public FutureThread(FutureFunction func,Object... objects) {
		this.func = func;
		this.args = objects;
	}

	public FutureThread(FutureFunction func) {
		this.func = func;
	}

	public void run() {
		
	}

	public void start() {
		if(func==null) {
			return ;
		}
		
		Context context = new Context(this.func, this.args);
		Scheduler.getInstance().run(context);
	}
}
