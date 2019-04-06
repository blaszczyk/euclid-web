package euclid.web.job;

import euclid.engine.SearchEngine;
import euclid.kpi.KpiMonitor;
import euclid.model.Board;
import euclid.problem.Problem;

public class Job {
	
	private final Problem problem;
	
	private final SearchEngine<Board> engine;
	
	private final KpiMonitor monitor;

	Job(Problem problem, SearchEngine<Board> engine, KpiMonitor monitor) {
		this.problem = problem;
		this.engine = engine;
		this.monitor = monitor;
	}

	public Problem getProblem() {
		return problem;
	}

	public SearchEngine<Board> getEngine() {
		return engine;
	}
	
	public KpiMonitor getKpiMonitor() {
		return monitor;
	}
	

}
