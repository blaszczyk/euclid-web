package euclid.web.job;

import java.util.Optional;

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

	public Problem problem() {
		return problem;
	}

	public Optional<Board> solution() {
		return engine.firstSolution();
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
