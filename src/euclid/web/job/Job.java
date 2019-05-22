package euclid.web.job;

import java.util.List;

import euclid.engine.SearchEngine;
import euclid.kpi.KpiReport;
import euclid.sets.Board;
import euclid.problem.Problem;

public class Job {
	
	private final Problem problem;
	
	private final SearchEngine<Board> engine;
	
	private KpiReport lastKpiReport;

	Job(final Problem problem, final SearchEngine<Board> engine) {
		this.problem = problem;
		this.engine = engine;
	}

	public Problem problem() {
		return problem;
	}

	public List<Board> solutions() {
		return engine.solutions();
	}

	public boolean finished() {
		return engine.finished();
	}
	
	public KpiReport kpiReport() {
		return lastKpiReport;
	}

	void start() {
		engine.start();
	}

	public void halt() {
		engine.halt();
	}
	
	public void consumeKpi(final KpiReport report) {
		lastKpiReport = report;
	}
	
}
