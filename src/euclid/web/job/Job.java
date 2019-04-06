package euclid.web.job;

import euclid.alg.engine.ThreadedSearchEngine;
import euclid.kpi.KpiMonitor;
import euclid.model.Board;
import euclid.problem.Problem;

public class Job {
	
	private final Problem problem;
	
	private final ThreadedSearchEngine<Board> engine;
	
	private final KpiMonitor monitor;

	Job(Problem problem, ThreadedSearchEngine<Board> engine, KpiMonitor monitor) {
		this.problem = problem;
		this.engine = engine;
		this.monitor = monitor;
	}

	public Problem getProblem() {
		return problem;
	}

	public ThreadedSearchEngine<Board> getEngine() {
		return engine;
	}
	
	public KpiMonitor getKpiMonitor() {
		return monitor;
	}
	

}
