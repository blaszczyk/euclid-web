package euclid.web.job;

import java.util.Collection;

import euclid.engine.SearchEngine;
import euclid.kpi.KpiMonitor;
import euclid.sets.Board;
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

	public Problem problem() {
		return problem;
	}

	public Collection<Board> solutions() {
		return engine.solutions();
	}

	public boolean finished() {
		return engine.finished();
	}

	void start() {
		engine.start(true);
		monitor.start();
	}

	void halt() {
		engine.halt();
		monitor.halt();
	}
	
}
